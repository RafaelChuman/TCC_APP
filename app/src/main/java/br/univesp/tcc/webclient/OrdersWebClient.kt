package br.univesp.tcc.webclient

import android.util.Log
import br.univesp.tcc.database.model.Orders
import br.univesp.tcc.webclient.model.DTOCreateOrders
import br.univesp.tcc.webclient.model.DTODeleteOrders
import br.univesp.tcc.webclient.model.DTOUpdateOrders

private const val TAG = "OrdersWebClient"

class OrdersWebClient {

    private val ordersService = RetrofitInicializador().ordersService

    suspend fun listAll(userToken: String): List<Orders>?
    {
        try {
            val listResp = ordersService.listAll(userToken)

            return listResp.body()
        } catch (e: Exception) {
            Log.e(TAG, "listAll - Error: ", e)

        }
        return null
    }


    suspend fun listByUser(userToken: String, userId: String): List<Orders>?
    {
         try {
            val listResp = ordersService.listByUser(userToken, userId)

             return listResp.body()
        } catch (e: Exception) {
             Log.e(TAG, "listByUser - Error: ", e)

        }
        return null
    }

    suspend fun findByCar(userToken: String, plate: String): List<Orders>?
    {
        try {
            val listResp = ordersService.listByCar(userToken, plate)

            return listResp.body()
        } catch (e: Exception) {
            Log.e(TAG, "findByCar - Error: ", e)

        }
        return null
    }

    suspend fun create(userToken: String, orders: List<Orders>): Orders?
    {
        try {
            val listResp = ordersService.create(userToken, orders)

            return listResp.body()
        } catch (e: Exception) {
            Log.e(TAG, "create - Error: ", e)
        }
        return null
    }

    suspend fun update(userToken: String, data: Orders): Orders?
    {
        try {
            val listResp = ordersService.update(userToken, data)

            return listResp.body()
        } catch (e: Exception) {
            Log.e(TAG, "update - Error: ", e)

        }
        return null
    }

    suspend fun delete(userToken: String, data: DTODeleteOrders): Orders?
    {
        try {
            val listResp = ordersService.delete(userToken, data)

            return listResp.body()
        } catch (e: Exception) {
            Log.e(TAG, "delete - Error: ", e)

        }
        return null
    }
}