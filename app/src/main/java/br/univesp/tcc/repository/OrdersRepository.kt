package br.univesp.tcc.repository

import android.content.Context
import android.util.Log
import androidx.room.FtsOptions
import br.univesp.tcc.database.dao.OrdersDAO
import br.univesp.tcc.database.dao.UserDao
import br.univesp.tcc.database.model.Car
import br.univesp.tcc.database.model.Orders
import br.univesp.tcc.database.model.OrdersCarUser
import br.univesp.tcc.database.model.User
import br.univesp.tcc.extensions.dataStore
import br.univesp.tcc.extensions.tokenDataStore
import br.univesp.tcc.webclient.OrdersWebClient
import br.univesp.tcc.webclient.UserWebClient
import br.univesp.tcc.webclient.model.DTODeleteOrders
import br.univesp.tcc.webclient.model.DTODeleteUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

private const val TAG = "OrdersRepository"

class OrdersRepository(
    private val ordersDao: OrdersDAO,
    private val ordersWebClient: OrdersWebClient,
    private val ctx: Context
) {

    private suspend fun getToken(): String {
        val preferences = ctx.dataStore.data.firstOrNull()

        if (preferences == null) return ""

        val userToken = preferences[tokenDataStore]

        if (userToken.isNullOrEmpty()) return ""

        return userToken
    }

    suspend fun getOrders(): List<OrdersCarUser>? {

        //syncOrders()

        val orders = ordersDao.getAll().firstOrNull()

        Log.i(TAG, "getOrders - orders: $orders")

        return orders
    }

    public suspend fun syncOrders() {

        Log.i(TAG, "syncOrders")

        val userToken = getToken()

        val ordersWeb = ordersWebClient.listAll(userToken) ?: listOf<Orders>()
        val ordersDAO = ordersDao.getAll().firstOrNull() ?: listOf<OrdersCarUser>()
        val updList: MutableList<Orders> = mutableListOf<Orders>()
        val addList: MutableList<Orders> = mutableListOf<Orders>()
        val dltList: MutableList<String> = mutableListOf<String>()

        for (ordWeb in ordersWeb) {
            val ordDAO = ordersDAO.find { dao -> dao.orderId == ordWeb.orderId }

            val needToInsert = (ordDAO == null)
            val needToUpdate = ((ordDAO != null) && (ordDAO.updated < ordWeb.updated))

            if (needToInsert) addList.add(ordWeb)

            if(needToUpdate) updList.add(ordWeb)
        }

        for (ordDAO in ordersDAO) {
            val ordWeb = ordersWeb.find { web -> web.orderId == ordDAO.orderId }

            val needToDelete = (ordWeb == null)

            if (needToDelete) dltList.add(ordDAO.orderId)
        }

        Log.i(TAG, "syncOrders - addList: $addList")
        if (addList.isNotEmpty()) ordersDao.insert(addList)

        Log.i(TAG, "syncOrders - updList: $updList")
        if (updList.isNotEmpty()) ordersDao.update(updList)

        Log.i(TAG, "syncOrders - dltList: $dltList")
        if (dltList.isNotEmpty())  ordersDao.purge(dltList)
    }

    suspend  fun getById(id: String): List<Orders>? {
        Log.i(TAG, "getById - id: $id")

        return ordersDao.getById(listOf(id)).firstOrNull()
    }


    suspend fun insert(newOrders: Orders) {

        Log.i(TAG, "insert - newOrders: $newOrders")
        val userToken = getToken()

        ordersDao.insert(listOf(newOrders))

        val userInserted = ordersWebClient.create(userToken, listOf(newOrders))

        Log.i(TAG, "insert - userInserted: $userInserted")
    }

    suspend fun update(updateOrders: Orders) {

        Log.i(TAG, "update - updateOrders: $updateOrders")
        val userToken = getToken()

        val ordersSearched = ordersDao.getById(listOf(updateOrders.orderId)).firstOrNull()
        Log.i(TAG, "update - ordersSearched: $ordersSearched")

        if (ordersSearched.isNullOrEmpty()) return

        ordersDao.update(listOf(updateOrders))

        val ordersUpdated = ordersWebClient.update(userToken, updateOrders)
        Log.i(TAG, "update - ordersUpdated: $ordersUpdated")
    }

    suspend fun delete(idList: List<String>) {

        Log.i(TAG, "delete")
        if (idList.isEmpty()) return

        val userToken = getToken()

        val data = DTODeleteOrders(
            id = idList
        )

        Log.i(TAG, "delete - idList: $idList")
        ordersDao.remove(idList)

        val ordersDeleted = ordersWebClient.delete(userToken, data)
        Log.i(TAG, "delete - ordersDeleted: $ordersDeleted")

    }
}