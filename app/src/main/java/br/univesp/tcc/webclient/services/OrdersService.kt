package br.univesp.tcc.webclient.services

import br.univesp.tcc.database.model.Car
import br.univesp.tcc.database.model.Orders
import br.univesp.tcc.webclient.model.DTOCreateCar
import br.univesp.tcc.webclient.model.DTOCreateOrders
import br.univesp.tcc.webclient.model.DTODeleteCar
import br.univesp.tcc.webclient.model.DTODeleteOrders
import br.univesp.tcc.webclient.model.DTOUpdateCar
import br.univesp.tcc.webclient.model.DTOUpdateOrders
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface OrdersService {

    @POST("orders")
    suspend fun create(@Header("Authorization") token: String, @Body data: List<Orders>): Response<Orders>

    @GET("orders")
    suspend fun listByUser(@Header("Authorization") token: String, @Body userId: String) : Response<List<Orders>>

    @GET("orders")
    suspend fun listByCar(@Header("Authorization") token: String, @Body plate: String) : Response<List<Orders>>

    @HTTP(method = "DELETE", path = "orders", hasBody = true)
    suspend fun delete(@Header("Authorization") token: String, @Body data: DTODeleteOrders): Response<Orders>

    @PUT("orders")
    suspend fun  update(@Header("Authorization") token: String, @Body data: List<Orders>): Response<Orders>

    @GET("orders")
    suspend fun listAll(@Header("Authorization") token: String):  Response<List<Orders>>
}
