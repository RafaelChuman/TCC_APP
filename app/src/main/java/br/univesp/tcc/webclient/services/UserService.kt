package br.univesp.tcc.webclient.services

import br.univesp.tcc.database.model.User
import br.univesp.tcc.webclient.model.DTOCreateUser
import br.univesp.tcc.webclient.model.DTODeleteUser
import br.univesp.tcc.webclient.model.DTOUpdateUser
import retrofit2.Response
import retrofit2.http.GET

interface UserService {

    @GET("user")
    suspend fun create(data: DTOCreateUser): Response< List<User>>

    @GET("user")
    suspend fun delete(data: DTODeleteUser): Response< List<User>>

    @GET("user")
    suspend fun update(user: DTOUpdateUser): Response< List<User>>

    @GET("user")
    suspend fun list(): Response< List<User>>

    @GET("user")
    suspend fun findByUserName(userName: String): Response< List<User>>

    @GET("user")
    suspend fun findById(id: String): Response< List<User>>

    @GET("user")
    suspend fun listAllUsersGroupedByMonth(): Response< List<User>>
}