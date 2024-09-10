package br.univesp.tcc.webclient

import android.util.Log
import br.univesp.tcc.database.model.UserToken
import br.univesp.tcc.webclient.model.AuthenticateDTO
import retrofit2.Response

private const val TAG = "AuthenticationWebClient"

class AuthenticationWebClient {

    private val authenticationService = RetrofitInicializador().authenticationService

    suspend fun authenticate(username: String, password: String): Response<UserToken>?
    {

        try {
            val data =  AuthenticateDTO(username, password)

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