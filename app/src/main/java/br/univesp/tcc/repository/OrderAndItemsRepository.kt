package br.univesp.tcc.repository

import android.content.Context
import android.util.Log
import br.univesp.tcc.database.dao.OrderAndItemsDAO
import br.univesp.tcc.database.model.OrderAndItems
import br.univesp.tcc.database.model.Orders
import br.univesp.tcc.database.model.OrdersCarUser
import br.univesp.tcc.extensions.dataStore
import br.univesp.tcc.extensions.tokenDataStore
import br.univesp.tcc.webclient.OrderAndItemsWebClient
import br.univesp.tcc.webclient.model.DTODeleteOrders
import kotlinx.coroutines.flow.firstOrNull

private const val TAG = "OrderAndItemsRepository"

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
        val addList: MutableList<OrderAndItems> = mutableListOf<OrderAndItems>()
        val dltList: MutableList<String> = mutableListOf<String>()

        Log.i(TAG, "syncOrderAndItems - orderAndItemsWeb: $orderAndItemsWeb")
        Log.i(TAG, "syncOrderAndItems - orderAndItemsDAO: $orderAndItemsDAO")

        for (ordItmWeb in orderAndItemsWeb) {
            val ordDAO = orderAndItemsDAO.find { dao -> dao.id == ordItmWeb.id }

            val needToInsert = (ordDAO == null)
            val needToUpdate = ((ordDAO != null) && (ordDAO.updated < ordItmWeb.updated))

            if (needToInsert) addList.add(ordItmWeb)

            if(needToUpdate) updList.add(ordItmWeb)
        }

        for (ordItmDAO in orderAndItemsDAO) {
            val ordWeb = orderAndItemsWeb.find { web -> web.id == ordItmDAO.id }

            val needToDelete = (ordWeb == null)

            if (needToDelete) dltList.add(ordItmDAO.id)
        }

        Log.i(TAG, "syncOrderAndItems - addList: $addList")
        if (addList.isNotEmpty()) orderItemsDao.insert(addList)

        Log.i(TAG, "syncOrderAndItems - updList: $updList")
        if (updList.isNotEmpty()) orderItemsDao.update(updList)

        Log.i(TAG, "syncOrderAndItems - dltList: $dltList")
        if (dltList.isNotEmpty())  orderItemsDao.purgeByID(dltList)
    }

    suspend  fun getById(id: String): List<OrderAndItems>? {
        Log.i(TAG, "getById - id: $id")

        return orderItemsDao.getByID(listOf(id))?.firstOrNull()
    }

    suspend fun insert(newOrderAndItems:  List<OrderAndItems>) {

        if(newOrderAndItems.isEmpty()) return

        Log.i(TAG, "save - newOrderAndItems: $newOrderAndItems")
        val userToken = getToken()

        orderItemsDao.insert(newOrderAndItems)

        val orderAndItemsInserted = orderItemsWebClient.save(userToken, newOrderAndItems)
        Log.i(TAG, "save - orderAndItemsInserted: $orderAndItemsInserted")
    }


    suspend fun update(orderAndItemsUpdate: List<OrderAndItems>) {

        Log.i(TAG, "update - orderAndItemsUpdate: $orderAndItemsUpdate")
        val addList: MutableList<OrderAndItems> = mutableListOf<OrderAndItems>()
        val updList: MutableList<OrderAndItems> = mutableListOf<OrderAndItems>()
        val delList: MutableList<String> = mutableListOf<String>()

        if(orderAndItemsUpdate.isEmpty()) return

        val orderID = orderAndItemsUpdate[0].orderId ?: ""
        Log.i(TAG, "update - orderID: $orderID")

        val orderAndItemsDAO= orderItemsDao.getByOrderID(orderID)?.firstOrNull() ?: listOf<OrderAndItems>()
        Log.i(TAG, "update - orderAndItemsDAO: $orderAndItemsDAO")

        if(orderAndItemsDAO.isEmpty()) return

        for (ordItmUpd in orderAndItemsUpdate) {
            val ordItmDAO = orderAndItemsDAO.find { dao -> dao.id == ordItmUpd.id }

            if (ordItmDAO == null)
            {
                addList.add(ordItmUpd)
                break
            }

            if (ordItmDAO.updated < ordItmUpd.updated) {
                updList.add(ordItmUpd)
            }
        }

        for (ordItmDAO in orderAndItemsDAO) {
            val  ordItmUpd = orderAndItemsUpdate.find { upd -> upd.id == ordItmDAO.id }

            if (ordItmUpd == null) delList.add(ordItmDAO.id)
        }

        purgeById(delList)

        insert(addList)

        if (updList.isNotEmpty()) orderItemsDao.update(updList)

    }

    private suspend fun purgeById(idList: List<String>) {

        if (idList.isEmpty()) return
        Log.i(TAG, "delete - idList: $idList")

        val userToken = getToken()



        orderItemsDao.purgeByID(idList)

        val ordersDeleted = orderItemsWebClient.purgeById(userToken, idList)
        Log.i(TAG, "delete - ordersDeleted: $ordersDeleted")
    }


    suspend fun purgeByOrderId(orderIdList: List<String>) {

        Log.i(TAG, "purgeByOrderId")
        if (orderIdList.isEmpty()) return

        val userToken = getToken()

        Log.i(TAG, "purgeByOrderId - orderIdList: $orderIdList")
        orderItemsDao.purgeByOrderID(orderIdList)

        val ordersDeleted = orderItemsWebClient.purgeByOrderId(userToken, orderIdList)
        Log.i(TAG, "purgeByOrderId - ordersDeleted: $ordersDeleted")
    }
}