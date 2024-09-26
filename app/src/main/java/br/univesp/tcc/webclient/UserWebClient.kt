package br.univesp.tcc.webclient

import android.util.Log
import br.univesp.tcc.database.model.DeleteResult
import br.univesp.tcc.database.model.InsertResult
import br.univesp.tcc.database.model.User
import br.univesp.tcc.webclient.model.DTOCreateUser
import br.univesp.tcc.webclient.model.DTODeleteUser
import br.univesp.tcc.webclient.model.DTOUpdateUser
import retrofit2.Response

private const val TAG = "UserWebClient"

class UserWebClient {

    private val userService = RetrofitInicializador().userService

    suspend fun create(userToken: String, user: User): Response<InsertResult>?
    {
        try {
            val listResp = userService.create(userToken, user)

            Log.i(TAG, "create - listResp: $listResp")
            return listResp

        } catch (e: Exception) {
            Log.e(TAG, "create - Error: ", e)
        }
        return null
    }

    suspend fun update(userToken: String, data: User): User?
    {
        try {
            val listResp = userService.update(userToken, data)

            Log.i(TAG, "update - listResp: $listResp")
            return listResp.body()

        } catch (e: Exception) {
            Log.e(TAG, "update - Error: ", e)
        }
        return null
    }

    suspend fun delete(userToken: String, data: DTODeleteUser): Response<DeleteResult>?
    {
        try {
            val listResp = userService.delete(userToken, data)

            Log.i(TAG, "delete - listResp: $listResp")
            return listResp

        } catch (e: Exception) {
            Log.e(TAG, "delete - Error: ", e)
        }
        return null
    }

    suspend fun list(userToken: String): List<User>?
    {
        try {
            val listResp = userService.list(userToken)

            Log.i(TAG, "userToken - listResp: $listResp")
            return listResp.body()

        } catch (e: Exception) {
            Log.e(TAG, "userToken - Error: ", e)
        }
        return null
    }


    suspend fun findByUserName(userToken: String, userName: String): List<User>?
    {
         try {
            val listResp = userService.findByUserName(userToken, userName)

             Log.i(TAG, "findByUserName - listResp: $listResp")
             return listResp.body()
        } catch (e: Exception) {
             Log.e(TAG, "findByUserName - Error: ", e)
        }
        return null
    }

    suspend fun findById(userToken: String, id: String): List<User>?
    {
         try {
            val listResp = userService.findById(userToken, id)

             Log.i(TAG, "findById - listResp: $listResp")
             return listResp.body()
        } catch (e: Exception) {
             Log.e(TAG, "findById - Error: ", e)
        }
        return null
    }

    suspend fun listAllUsersGroupedByMonth(userToken: String): List<User>?
    {
        try {
            val listResp = userService.listAllUsersGroupedByMonth(userToken)

            Log.i(TAG, "listAllUsersGroupedByMonth - listResp: $listResp")
            return listResp.body()
        } catch (e: Exception) {
            Log.e(TAG, "listAllUsersGroupedByMonth - Error: ", e)

        }
        return null
    }
}