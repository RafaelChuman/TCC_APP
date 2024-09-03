package br.univesp.tcc.webclient

import android.util.Log
import br.univesp.tcc.database.model.OrderAndItems
import br.univesp.tcc.webclient.model.DTOCreateOrderAndItems
import br.univesp.tcc.webclient.model.DTODeleteOrderAndItems
import br.univesp.tcc.webclient.model.DTOListOrderAndItemsByOrder
import br.univesp.tcc.webclient.model.DTOListOrderAndItemsByUser

private const val TAG = "OrderAndItemsWebClient"

class OrderAndItemsWebClient {

    private val orderAndItemsService = RetrofitInicializador().orderAndItemsService

    suspend fun listByUser(data: DTOListOrderAndItemsByUser): List<OrderAndItems>?
    {
         try {
            val listResp = orderAndItemsService.listByUser(data)

             return listResp.body()
        } catch (e: Exception) {
             Log.e(TAG, "listByUser", e)

        }
        return null
    }

    suspend fun findByCar(data: DTOListOrderAndItemsByOrder): List<OrderAndItems>?
    {
        try {
            val listResp = orderAndItemsService.listByOrder(data)

            return listResp.body()
        } catch (e: Exception) {
            Log.e(TAG, "findByCar", e)

        }
        return null
    }

    suspend fun create(data: DTOCreateOrderAndItems): OrderAndItems?
    {
        try {
            val listResp = orderAndItemsService.create(data)

            return listResp.body()
        } catch (e: Exception) {
            Log.e(TAG, "create", e)

        }
        return null
    }


    suspend fun delete(data: DTODeleteOrderAndItems): OrderAndItems?
    {
        try {
            val listResp = orderAndItemsService.delete(data)

            return listResp.body()
        } catch (e: Exception) {
            Log.e(TAG, "delete", e)

        }
        return null
    }
}