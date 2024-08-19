package br.univesp.tcc.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import br.univesp.tcc.database.dao.UserTokenDAO
import br.univesp.tcc.extensions.userIdLogged
import br.univesp.tcc.webclient.AuthenticationWebClient

private const val TAG = "authenticateUser"

class AuthenticationRepository (private val authenticationDAO: UserTokenDAO,
                                private val authenticationWebClient: AuthenticationWebClient,
                                private val dataStore: DataStore<Preferences>){




    suspend fun authenticateUser(userName: String, password: String): Boolean{

        Log.i(TAG, "authenticateUser - userName: $userName, password: $password")

        authenticationWebClient.authenticate(userName, password)?.let {
            resp ->

            Log.i(TAG, "authenticateUser - resp.code: ${resp.code()}")

            if(resp.code() != 200)
                return false

            resp.body()?.let { token ->
                Log.i(TAG, "authenticateUser - token: $token")
                authenticationDAO.save(token)

                Log.i(TAG, "authenticateUser - token.userId: ${token.userId}")
                dataStore.edit { preferences ->
                    preferences[userIdLogged] = token.userId
                }

                return true
            }

        }
        return false
    }

}