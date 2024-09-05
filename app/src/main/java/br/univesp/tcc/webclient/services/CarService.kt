package br.univesp.tcc.webclient.services

import br.univesp.tcc.database.model.Car
import br.univesp.tcc.webclient.model.DTOCreateCar
import br.univesp.tcc.webclient.model.DTODeleteCar
import br.univesp.tcc.webclient.model.DTOUpdateCar
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface CarService {

    @POST("car")
    suspend fun create(@Body data: DTOCreateCar): Response<Car>

    @GET("car")
    suspend fun listCarByUser(@Body userId: String) : Response<List<Car>>

    @GET("car")
    suspend fun listAll() : Response<List<Car>>

    @GET("car")
    suspend fun listCarById(@Body id:List<String>) : Response<Car>

    @GET("car")
    suspend fun listCarByPlate(@Body plate: List<String>) : Response<List<Car>>

    @DELETE("car")
    suspend fun delete(@Body id: List<String>): Response<Car>

    @PUT("car")
    suspend fun  update(@Body data: DTOUpdateCar): Response<Car>
}
