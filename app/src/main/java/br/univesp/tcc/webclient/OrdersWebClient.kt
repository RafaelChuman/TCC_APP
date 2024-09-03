package br.univesp.tcc.webclient

import android.util.Log
import br.univesp.tcc.database.model.Orders
import br.univesp.tcc.webclient.model.DTOCreateOrders
import br.univesp.tcc.webclient.model.DTODeleteOrders
import br.univesp.tcc.webclient.model.DTOUpdateOrders

private const val TAG = "OrdersWebClient"

class OrdersWebClient {

    private val ordersService = RetrofitInicializador().ordersService

    suspend fun listByUser(userId: String): List<Orders>?
    {
         try {
            val listResp = ordersService.listByUser(userId)

             return listResp.body()
        } catch (e: Exception) {
             Log.e(TAG, "listByUser", e)

        }
        return null
    }

    suspend fun findByCar(plate: String): List<Orders>?
    {
        try {
            val listResp = ordersService.findByCar(plate)

            return listResp.body()
        } catch (e: Exception) {
            Log.e(TAG, "findByCar", e)

        }
        return null
    }

    suspend fun create(data: DTOCreateOrders): Orders?
    {
        try {
            val listResp = ordersService.create(data)

            return listResp.body()
        } catch (e: Exception) {
            Log.e(TAG, "create", e)

        }
        return null
    }

    suspend fun update(data: DTOUpdateOrders): Orders?
    {
        try {
            val listResp = ordersService.update(data)

            return listResp.body()
        } catch (e: Exception) {
            Log.e(TAG, "update", e)

        }
        return null
    }

    suspend fun delete(data: DTODeleteOrders): Orders?
    {
        try {
            val listResp = ordersService.delete(data)

            return listResp.body()
        } catch (e: Exception) {
            Log.e(TAG, "delete", e)

        }
        return null
    }
}