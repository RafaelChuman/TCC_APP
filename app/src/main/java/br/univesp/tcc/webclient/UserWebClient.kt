package br.univesp.tcc.webclient

import android.util.Log
import br.univesp.tcc.database.model.User
import br.univesp.tcc.webclient.model.DTOCreateUser
import br.univesp.tcc.webclient.model.DTODeleteUser
import br.univesp.tcc.webclient.model.DTOUpdateUser

private const val TAG = "UserWebClient"

class UserWebClient {

    private val userService = RetrofitInicializador().userService

    suspend fun create(user: User): List<User>?
    {
        try {
            val listResp = userService.create(listOf(user))

            return listResp.body()
        } catch (e: Exception) {
            Log.e(TAG, "create", e)
        }
        return null
    }

    suspend fun delete(data: DTODeleteUser): List<User>?
    {
        try {
            val listResp = userService.delete(data)

            return listResp.body()
        } catch (e: Exception) {
            Log.e(TAG, "delete", e)
        }
        return null
    }

    suspend fun update(data: DTOUpdateUser): List<User>?
    {
        try {
            val listResp = userService.update(data)

            return listResp.body()
        } catch (e: Exception) {
            Log.e(TAG, "update", e)
        }
        return null
    }


    suspend fun list(userToken:String): List<User>?
    {
        Log.i(TAG, "list")
        try {
            val listResp = userService.list(userToken)

            return listResp.body()
        } catch (e: Exception) {
            Log.e(TAG, "list - error:", e)
        }
        return null
    }


    suspend fun findByUserName(userName: String): List<User>?
    {
         try {
            val listResp = userService.findByUserName(userName)

             return listResp.body()
        } catch (e: Exception) {
            Log.e(TAG, "findByUserName", e)
        }
        return null
    }

    suspend fun findById(id: String): List<User>?
    {
         try {
            val listResp = userService.findById(id)

             return listResp.body()
        } catch (e: Exception) {
            Log.e(TAG, "findById", e)
        }
        return null
    }

    suspend fun listAllUsersGroupedByMonth(): List<User>?
    {
        try {
            val listResp = userService.listAllUsersGroupedByMonth()

            return listResp.body()
        } catch (e: Exception) {
            Log.e(TAG, "listAllUsersGroupedByMonth", e)

        }
        return null
    }
}