package br.univesp.tcc.webclient

import android.util.Log
import br.univesp.tcc.database.model.User

private const val TAG = "UserWebClient"

class UserWebClient {

    private val userService = RetrofitInicializador().userService

    suspend fun getUsers(): List<User>?
    {
        return try {
            val listResp = userService.getUsers()

            listResp.body()?.map { userResp -> userResp.user }
        } catch (e: Exception) {
            Log.e(TAG, "getUsers", e)
            null
        }
    }


    suspend fun loginUsers(): List<User>?
    {
        return try {
            val listResp = userService.getUsers()

            listResp.body()?.map { userResp -> userResp.user }
        } catch (e: Exception) {
            Log.e(TAG, "getUsers", e)
            null
        }
    }



}