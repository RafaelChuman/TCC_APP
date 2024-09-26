package br.univesp.tcc.webclient.services

import br.univesp.tcc.database.model.Car
import br.univesp.tcc.database.model.DeleteResult
import br.univesp.tcc.database.model.InsertResult
import br.univesp.tcc.database.model.User
import br.univesp.tcc.webclient.model.DTODeleteCar
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface CarService {

    @POST("car")
    suspend fun create(@Header("Authorization") token: String, @Body data: List<Car>): Response<InsertResult>

    @PUT("car")
    suspend fun  update(@Header("Authorization") token: String, @Body data: List<Car>): Response<Car>

    @HTTP(method = "DELETE", path = "user", hasBody = true)
    suspend fun delete(@Header("Authorization") token: String, @Body data: DTODeleteCar): Response<DeleteResult>

    @GET("car")
    suspend fun listAll(@Header("Authorization") token: String) : Response<List<Car>>

    @GET("car")
    suspend fun listCarByUser(@Header("Authorization") token: String, @Body userId: String) : Response<List<Car>>

    @GET("car")
    suspend fun listCarById(@Header("Authorization") token: String, @Body id:List<String>) : Response<Car>

    @GET("car")
    suspend fun listCarByPlate(@Header("Authorization") token: String, @Body plate: List<String>) : Response<List<Car>>




}
