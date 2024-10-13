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

    suspend fun listByUser(userToken: String, userId: List<String>): List<OrderAndItems>?
    {
         try {
            val listResp = orderAndItemsService.listByUser(userToken, userId)

             return listResp.body()
        } catch (e: Exception) {
             Log.e(TAG, "listByUser - error: ", e)
        }
        return null
    }

    suspend fun getAll(userToken: String): List<OrderAndItems>?
    {
        try {
            val listResp = orderAndItemsService.getAll(userToken)

            return listResp.body()
        } catch (e: Exception) {
            Log.e(TAG, "getAll - error: ", e)
        }
        return null
    }

    suspend fun findByCar(userToken: String, data: DTOListOrderAndItemsByOrder): List<OrderAndItems>?
    {
        try {
            val listResp = orderAndItemsService.listByOrder(userToken, data)

            return listResp.body()
        } catch (e: Exception) {
            Log.e(TAG, "findByCar - error: ", e)
        }
        return null
    }

    suspend fun create(userToken: String, orderAndItems: List<OrderAndItems>): OrderAndItems?
    {
        try {
            val listResp = orderAndItemsService.create(userToken, orderAndItems)

            return listResp.body()
        } catch (e: Exception) {
            Log.e(TAG, "create - error: ", e)
        }
        return null
    }


    suspend fun delete(userToken: String, data: DTODeleteOrderAndItems): OrderAndItems?
    {
        try {
            val listResp = orderAndItemsService.delete(userToken, data)

            return listResp.body()
        } catch (e: Exception) {
            Log.e(TAG, "delete - error: ", e)
        }
        return null
    }
}