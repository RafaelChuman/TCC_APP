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
import retrofit2.http.POST

interface OrdersService {

    @POST("orders")
    suspend fun create(@Body data: DTOCreateOrders): Response<Orders>

    @POST("orders")
    suspend fun listByUser(@Body userId: String) : Response<List<Orders>>

    @POST("orders")
    suspend fun findByCar(@Body plate: String) : Response<List<Orders>>

    @POST("orders")
    suspend fun delete(@Body data: DTODeleteOrders): Response<Orders>

    @POST("orders")
    suspend fun  update(@Body data: DTOUpdateOrders): Response<Orders>
}
