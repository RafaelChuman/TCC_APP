package br.univesp.tcc.webclient.services

import br.univesp.tcc.database.model.Car
import br.univesp.tcc.webclient.model.DTOCreateCar
import br.univesp.tcc.webclient.model.DTODeleteCar
import br.univesp.tcc.webclient.model.DTOUpdateCar
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CarService {

    @POST("car")
    suspend fun create(@Body data: DTOCreateCar): Response<Car>

    @POST("car")
    suspend fun listCarByUser(@Body userId: String) : Response<List<Car>>

    @POST("car")
    suspend fun delete(@Body data: DTODeleteCar): Response<Car>

    @POST("car")
    suspend fun  update(@Body data: DTOUpdateCar): Response<Car>
}
