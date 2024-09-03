package br.univesp.tcc.webclient.services

import br.univesp.tcc.database.model.Car
import br.univesp.tcc.database.model.Item
import br.univesp.tcc.webclient.model.DTOCreateCar
import br.univesp.tcc.webclient.model.DTOCreateItem
import br.univesp.tcc.webclient.model.DTODeleteCar
import br.univesp.tcc.webclient.model.DTODeleteItem
import br.univesp.tcc.webclient.model.DTOUpdateCar
import br.univesp.tcc.webclient.model.DTOUpdateItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ItemService {

    @POST("item")
    suspend fun create(@Body data: DTOCreateItem): Response<Item>

    @POST("item")
    suspend fun findByName(@Body name: String) : Response<List<Item>>

    @POST("item")
    suspend fun delete(@Body data: DTODeleteItem): Response<Item>

    @POST("item")
    suspend fun  update(@Body data: DTOUpdateItem): Response<Item>
}
