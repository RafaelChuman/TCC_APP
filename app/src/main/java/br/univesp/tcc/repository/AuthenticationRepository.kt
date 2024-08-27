package br.univesp.tcc.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import br.univesp.tcc.database.dao.UserTokenDAO
import br.univesp.tcc.database.model.UserToken
import br.univesp.tcc.extensions.dataStore
import br.univesp.tcc.extensions.userIdLogged
import br.univesp.tcc.webclient.AuthenticationWebClient

private const val TAG = "AuthenticationRepository"

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


    suspend fun checkUserAuthenticated(): UserToken?{

        Log.i(TAG, "checkUserAuthenticated")

        var userId : String = ""

        dataStore.data.collect { preferences ->
            preferences[userIdLogged]?.let { usrId ->
                userId = usrId
            }
        }

        if (userId == "") return null

        val token = authenticationDAO.getToken(userId)

        if (token == null) return null

        val userTokenResp = authenticationWebClient.isTokenValid(token.token)

        if(userTokenResp?.code() != 200) return null

        val respBody = userTokenResp.body()

        if(respBody == true) return token

        return null
    }

    suspend fun logout(){

        Log.i(TAG, "logout *** ")

        //var userId : String = ""

//        dataStore.data.collect { preferences ->
//            preferences[userIdLogged]?.let { usrId ->
//                userId = usrId
//                //Log.i(TAG, "logout - usrId $usrId")
//            }
//        }
        val token = authenticationDAO.delAllToken()
        Log.i(TAG, "logout - delToken: $token")

        dataStore.edit { preferences ->
            preferences[userIdLogged] = ""
            Log.i(TAG, "logout - preferences.remove")
        }


    }
}