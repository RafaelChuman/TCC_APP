package br.univesp.tcc.webclient.services

import br.univesp.tcc.database.model.Car
import br.univesp.tcc.database.model.OrderAndItems
import br.univesp.tcc.database.model.Orders
import br.univesp.tcc.webclient.model.DTOCreateCar
import br.univesp.tcc.webclient.model.DTOCreateOrderAndItems
import br.univesp.tcc.webclient.model.DTOCreateOrders
import br.univesp.tcc.webclient.model.DTODeleteCar
import br.univesp.tcc.webclient.model.DTODeleteOrderAndItems
import br.univesp.tcc.webclient.model.DTODeleteOrders
import br.univesp.tcc.webclient.model.DTOListOrderAndItemsByOrder
import br.univesp.tcc.webclient.model.DTOListOrderAndItemsByUser
import br.univesp.tcc.webclient.model.DTOUpdateCar
import br.univesp.tcc.webclient.model.DTOUpdateOrders
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.POST

interface OrderAndItemsService {

    @POST("orderAndItems")
    suspend fun create(@Header("Authorization") token: String, @Body orderAndItems: List<OrderAndItems>): Response<OrderAndItems>

    @GET("orderAndItems")
    suspend fun listByOrder(@Header("Authorization") token: String, @Body data: DTOListOrderAndItemsByOrder) : Response<List<OrderAndItems>>

    @GET("orderAndItems")
    suspend fun listByUser(@Header("Authorization") token: String, @Body userId: List<String>) : Response<List<OrderAndItems>>

    @GET("orderAndItems")
    suspend fun getAll(@Header("Authorization") token: String) : Response<List<OrderAndItems>>

    @HTTP(method = "DELETE", path = "user", hasBody = true)
    suspend fun delete(@Header("Authorization") token: String, @Body data: DTODeleteOrderAndItems): Response<OrderAndItems>

}
