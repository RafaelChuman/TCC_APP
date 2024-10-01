package br.univesp.tcc.repository

import android.content.Context
import android.util.Log
import androidx.room.FtsOptions
import br.univesp.tcc.database.dao.OrdersDAO
import br.univesp.tcc.database.dao.UserDao
import br.univesp.tcc.database.model.Car
import br.univesp.tcc.database.model.Orders
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

    suspend fun getOrders(): List<Orders>? {

        syncOrders()

        val orders = ordersDao.getAll().firstOrNull()

        Log.i(TAG, "getOrders - orders: $orders")

        return orders
    }

    private suspend fun syncOrders() {

        Log.i(TAG, "syncOrders")

        val userToken = getToken()

        val ordersWeb = ordersWebClient.listAll(userToken) ?: listOf<Orders>()
        val orders = ordersDao.getAll().firstOrNull() ?: listOf<Orders>()
        val updList: MutableList<Orders> = mutableListOf<Orders>()

        for (ord in ordersWeb) {
            val updOrders = orders.find { item -> item.id == ord.id }
            if (updOrders == null || updOrders.updated < ord.updated) {
                updList.add(ord)
            }
        }

        Log.i(TAG, "syncOrders - updList: $updList")
        if (updList.isNotEmpty()) ordersDao.save(updList)
    }

    suspend  fun getById(id: String): List<Orders>? {
        Log.i(TAG, "getById - id: $id")

        return ordersDao.getById(listOf(id)).firstOrNull()
    }


    suspend fun insert(newOrders: Orders) {

        Log.i(TAG, "insert - newOrders: $newOrders")
        val userToken = getToken()

        ordersDao.save(listOf(newOrders))

        val userInserted = ordersWebClient.create(userToken, newOrders)

        Log.i(TAG, "insert - userInserted: $userInserted")
    }

    suspend fun update(updateOrders: Orders) {

        Log.i(TAG, "update - updateOrders: $updateOrders")
        val userToken = getToken()

        val ordersSearched = ordersDao.getById(listOf(updateOrders.id)).firstOrNull()

        if (ordersSearched.isNullOrEmpty()) return

        ordersDao.save(listOf(updateOrders))

        val ordersUpdated = ordersWebClient.update(userToken, updateOrders)

        Log.i(TAG, "update - ordersUpdated: $ordersUpdated")
    }

    suspend fun delete(idList: List<String>) {

        if (idList.isNotEmpty()) return

        val userToken = getToken()

        val user = ordersDao.getById(idList).firstOrNull()

        if (user.isNullOrEmpty()) return

        val data = DTODeleteOrders(
            id = idList
        )

        ordersDao.remove(data.id)

        val ordersDeleted = ordersWebClient.delete(userToken, data)
        Log.i(TAG, "update - ordersDeleted: $ordersDeleted")

    }
}