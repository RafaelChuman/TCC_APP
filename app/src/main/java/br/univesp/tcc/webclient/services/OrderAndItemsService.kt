package br.univesp.tcc.webclient.services

import br.univesp.tcc.database.model.OrderAndItems
import br.univesp.tcc.webclient.model.DTODeleteOrderAndItemsById
import br.univesp.tcc.webclient.model.DTODeleteOrderAndItemsByOrderId
import br.univesp.tcc.webclient.model.DTOListOrderAndItemsByOrder
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.POST

interface OrderAndItemsService {

    @POST("orderAndItems")
    suspend fun save(@Header("Authorization") token: String, @Body orderAndItems: List<OrderAndItems>): Response<List<OrderAndItems>>

    @GET("orderAndItems")
    suspend fun listByOrder(@Header("Authorization") token: String, @Body data: DTOListOrderAndItemsByOrder) : Response<List<OrderAndItems>>

    @GET("orderAndItems")
    suspend fun listByUser(@Header("Authorization") token: String, @Body userId: List<String>) : Response<List<OrderAndItems>>

    @GET("orderAndItems")
    suspend fun getAll(@Header("Authorization") token: String) : Response<List<OrderAndItems>>

    @HTTP(method = "DELETE", path = "orderAndItems", hasBody = true)
    suspend fun purgeById(@Header("Authorization") token: String, @Body id: DTODeleteOrderAndItemsById): Response<List<OrderAndItems>>

    @HTTP(method = "DELETE", path = "orderAndItems", hasBody = true)
    suspend fun purgeByOrderId(@Header("Authorization") token: String, @Body orderId: DTODeleteOrderAndItemsByOrderId): Response<List<OrderAndItems>>

}
