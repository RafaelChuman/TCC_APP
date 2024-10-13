package br.univesp.tcc.webclient

import android.util.Log
import br.univesp.tcc.database.model.OrderAndItems
import br.univesp.tcc.webclient.model.DTODeleteOrderAndItemsById
import br.univesp.tcc.webclient.model.DTODeleteOrderAndItemsByOrderId
import br.univesp.tcc.webclient.model.DTOListOrderAndItemsByOrder

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

    suspend fun save(userToken: String, orderAndItems: List<OrderAndItems>): List<OrderAndItems>?
    {
        try {
            val listResp = orderAndItemsService.save(userToken, orderAndItems)

            return listResp.body()
        } catch (e: Exception) {
            Log.e(TAG, "create - error: ", e)
        }
        return null
    }


    suspend fun purgeById(userToken: String, id: List<String>): List<OrderAndItems>?
    {
        try {
            val data = DTODeleteOrderAndItemsById(
                id = id
            )

            val listResp = orderAndItemsService.purgeById(userToken, data)

            return listResp.body()
        } catch (e: Exception) {
            Log.e(TAG, "purgeById - error: ", e)
        }
        return null
    }

    suspend fun purgeByOrderId(userToken: String, orderId: List<String>): List<OrderAndItems>?
    {
        try {

            val data = DTODeleteOrderAndItemsByOrderId(
                orderId = orderId
            )

            val listResp = orderAndItemsService.purgeByOrderId(userToken, data)

            return listResp.body()
        } catch (e: Exception) {
            Log.e(TAG, "purgeByOrderId - error: ", e)
        }
        return null
    }
}