package br.univesp.tcc.webclient

import android.util.Log
import br.univesp.tcc.database.model.AuthenticatePost
import br.univesp.tcc.database.model.UserToken
import retrofit2.Response

private const val TAG = "AuthenticationWebClient"

class AuthenticationWebClient {

    private val authenticationService = RetrofitInicializador().authenticationService

    suspend fun authenticate(username: String, password: String): Response<UserToken>?
    {

        try {
            val data =  AuthenticatePost(username, password)

            val token = authenticationService.authenticate(data)
            return token

        } catch (e: Exception) {

            Log.e(TAG, "authenticate", e)
            return null
        }
    }


    suspend fun isTokenValid(token: String): Response<Boolean>?
    {
        try {
            return authenticationService.isTokenValid(token)

        } catch (e: Exception) {
            Log.e(TAG, "isTokenValid", e)
            return null
        }
    }
}