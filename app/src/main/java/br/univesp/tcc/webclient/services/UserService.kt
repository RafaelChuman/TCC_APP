package br.univesp.tcc.webclient.services

import br.univesp.tcc.database.model.DeleteResult
import br.univesp.tcc.database.model.InsertResult
import br.univesp.tcc.database.model.User
import br.univesp.tcc.webclient.model.DTOCreateUser
import br.univesp.tcc.webclient.model.DTODeleteUser
import br.univesp.tcc.webclient.model.DTOUpdateUser
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT

interface UserService {

    @POST("user")
    suspend fun create(@Header("Authorization") token: String, @Body data: User): Response<InsertResult>

    @PUT("user")
    suspend fun update(@Header("Authorization") token: String, @Body user: User): Response<User>

    @HTTP(method = "DELETE", path = "user", hasBody = true)
    suspend fun delete(@Header("Authorization") token: String, @Body data: DTODeleteUser): Response<DeleteResult>

    @GET("user")
    suspend fun list(@Header("Authorization") token: String): Response< List<User>>

    @GET("user")
    suspend fun findByUserName(@Header("Authorization") token: String, userName: String): Response< List<User>>

    @GET("user")
    suspend fun findById(@Header("Authorization") token: String, id: String): Response< List<User>>

    @GET("user")
    suspend fun listAllUsersGroupedByMonth(@Header("Authorization") token: String): Response< List<User>>
}