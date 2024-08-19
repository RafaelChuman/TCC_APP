package br.univesp.tcc.webclient.services

import br.univesp.tcc.webclient.model.UserResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface UserService {

    @GET("user")
    suspend fun getUsers(): Response< List<UserResponse>>

    @GET("")
    suspend fun authenticate(): Response< List<UserResponse>>
}