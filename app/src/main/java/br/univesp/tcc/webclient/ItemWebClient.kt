package br.univesp.tcc.webclient

import android.util.Log
import br.univesp.tcc.database.model.Item
import br.univesp.tcc.webclient.model.DTOCreateItem
import br.univesp.tcc.webclient.model.DTODeleteItem
import br.univesp.tcc.webclient.model.DTOUpdateItem

private const val TAG = "ItemWebClient"

class ItemWebClient {

    private val itemService = RetrofitInicializador().itemService

    suspend fun findByName(name: String): List<Item>?
    {
         try {
            val listResp = itemService.findByName(name)

             return listResp.body()
        } catch (e: Exception) {
             Log.e(TAG, "findByName", e)

        }
        return null
    }

    suspend fun create(data: DTOCreateItem): Item?
    {
        try {
            val listResp = itemService.create(data)

            return listResp.body()
        } catch (e: Exception) {
            Log.e(TAG, "create", e)

        }
        return null
    }

    suspend fun update(data: DTOUpdateItem): Item?
    {
        try {
            val listResp = itemService.update(data)

            return listResp.body()
        } catch (e: Exception) {
            Log.e(TAG, "update", e)

        }
        return null
    }

    suspend fun delete(data: DTODeleteItem): Item?
    {
        try {
            val listResp = itemService.delete(data)

            return listResp.body()
        } catch (e: Exception) {
            Log.e(TAG, "delete", e)

        }
        return null
    }
}