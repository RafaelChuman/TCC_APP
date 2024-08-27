package br.univesp.tcc.webclient.services

import br.univesp.tcc.database.model.AuthenticatePost
import br.univesp.tcc.database.model.UserToken
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthenticationService {


    @POST("/")
    suspend fun authenticate(@Body data: AuthenticatePost): Response<UserToken>

    @POST("/")
    suspend fun isTokenValid(@Header("Authorization") token: String): Response<Boolean>
}