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
import retrofit2.http.POST

interface OrderAndItemsService {

    @POST("orderAndItems")
    suspend fun create(@Body data: DTOCreateOrderAndItems): Response<OrderAndItems>

    @POST("orderAndItems")
    suspend fun listByOrder(@Body data: DTOListOrderAndItemsByOrder) : Response<List<OrderAndItems>>

    @POST("orderAndItems")
    suspend fun listByUser(@Body userId: String) : Response<List<OrderAndItems>>

    @POST("orderAndItems")
    suspend fun delete(@Body data: DTODeleteOrderAndItems): Response<OrderAndItems>

}
