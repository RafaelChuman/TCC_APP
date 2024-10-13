package br.univesp.tcc.repository

import android.content.Context
import android.util.Log
import br.univesp.tcc.database.dao.OrderAndItemsDAO
import br.univesp.tcc.database.model.OrderAndItems
import br.univesp.tcc.extensions.dataStore
import br.univesp.tcc.extensions.tokenDataStore
import br.univesp.tcc.webclient.OrderAndItemsWebClient
import br.univesp.tcc.webclient.model.DTODeleteOrders
import kotlinx.coroutines.flow.firstOrNull

private const val TAG = "OrderItemsRepository"

class OrderAndItemsRepository(
    private val orderItemsDao: OrderAndItemsDAO,
    private val orderItemsWebClient: OrderAndItemsWebClient,
    private val ctx: Context
) {

    private suspend fun getToken(): String {
        val preferences = ctx.dataStore.data.firstOrNull()

        if (preferences == null) return ""

        val userToken = preferences[tokenDataStore]

        if (userToken.isNullOrEmpty()) return ""

        return userToken
    }

    suspend fun getOrderItems(orderId : String): List<OrderAndItems>? {

        syncOrderAndItems()

        //val userToken = getToken()

        val orderAndItems = orderItemsDao.getByOrderID(orderId)?.firstOrNull() ?: listOf<OrderAndItems>()

        Log.i(TAG, "getOrderItems - orderAndItems: $orderAndItems")

        return orderAndItems
    }

    private suspend fun syncOrderAndItems() {

        Log.i(TAG, "syncOrderAndItems")

        val userToken = getToken()

        val orderAndItemsWeb = orderItemsWebClient.getAll(userToken) ?: listOf<OrderAndItems>()
        val orderAndItemsDAO = orderItemsDao.getAll()?.firstOrNull() ?: listOf<OrderAndItems>()
        val updList: MutableList<OrderAndItems> = mutableListOf<OrderAndItems>()
        val dltList: MutableList<String> = mutableListOf<String>()

        Log.i(TAG, "syncOrderAndItems - orderAndItemsWeb: $orderAndItemsWeb")

        for (ord in orderAndItemsWeb) {
            val updOrderAndItems = orderAndItemsDAO.find { item -> item.id == ord.id }
            if (updOrderAndItems == null || updOrderAndItems.updated < ord.updated) {
                updList.add(ord)
            }
        }

        Log.i(TAG, "syncOrderAndItems - updList: $updList")
        if (updList.isNotEmpty()) orderItemsDao.save(updList)

        for (ord in orderAndItemsDAO) {
            val updOrdAndItems = orderAndItemsWeb.find { web -> web.id == ord.id }
            Log.i(TAG, "syncOrderAndItems - dltList  orderAndItemsWeb: ${updOrdAndItems?.updated.toString()} == orderAndItemsDAO: ${ord.updated.toString()}")
            if (updOrdAndItems == null){  // ||  updCar.updated < carSqlite.updated) {
                dltList.add(ord.orderId)
            }
        }

        Log.i(TAG, "syncOrderAndItems - dltList: $dltList")
        if (dltList.isNotEmpty())  orderItemsDao.purgeByID(dltList)
    }

    suspend  fun getById(id: String): List<OrderAndItems>? {
        Log.i(TAG, "getById - id: $id")

        return orderItemsDao.getByID(listOf(id))?.firstOrNull()
    }


    suspend fun insert(newOrderAndItems:  List<OrderAndItems>) {

        Log.i(TAG, "insert - newOrderAndItems: $newOrderAndItems")
        val userToken = getToken()

        orderItemsDao.save(newOrderAndItems)

        val userInserted = orderItemsWebClient.create(userToken, newOrderAndItems)

        Log.i(TAG, "insert - userInserted: $userInserted")
    }

    suspend fun update(updateOrderAndItems: List<OrderAndItems>) {

        Log.i(TAG, "update - updateOrderAndItems: $updateOrderAndItems")
        val userToken = getToken()
        val updList: MutableList<OrderAndItems> = mutableListOf<OrderAndItems>()
        val delList: MutableList<OrderAndItems> = mutableListOf<OrderAndItems>()

        if(updateOrderAndItems.isEmpty()) return

        val orderID = updateOrderAndItems[0].orderId ?: ""

        val ordersSearched = orderItemsDao.getByOrderID(orderID)?.firstOrNull() ?: listOf<OrderAndItems>()

        for (ord in updateOrderAndItems) {
            val updItem = ordersSearched.find { item -> item.id == ord.id }

            if (updItem == null)
            {
                delList.add(ord)
                break
            }
            if (updItem.updated < ord.updated) {
                updList.add(ord)
            }
        }

        if(delList.isNotEmpty())
        {
            delete(delList.map { item -> item.id })
        }

        if(updList.isNotEmpty())
        {
            orderItemsDao.save(updList)

            //val ordersUpdated = orderItemsWebClient.update(userToken, updList)
            //Log.i(TAG, "update - ordersUpdated: $ordersUpdated")
        }
    }

    suspend fun delete(idList: List<String>) {

        if (idList.isNotEmpty()) return

        val userToken = getToken()

        val data = DTODeleteOrders(
            id = idList
        )

        orderItemsDao.purgeByID(data.id)

        //val ordersDeleted = orderItemsWebClient.delByID(userToken, data)
        //Log.i(TAG, "update - ordersDeleted: $ordersDeleted")

    }


    suspend fun deleteByOrderId(orderIdList: List<String>) {

        if (orderIdList.isNotEmpty()) return

        val userToken = getToken()

        val data = DTODeleteOrders(
            id = orderIdList
        )

        orderItemsDao.purgeByOrderID(data.id)

        //val ordersDeleted = orderItemsWebClient.delByID(userToken, data)
        //Log.i(TAG, "update - ordersDeleted: $ordersDeleted")

    }
}