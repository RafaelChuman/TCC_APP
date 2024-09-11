package br.univesp.tcc.repository

import android.util.Log
import br.univesp.tcc.database.dao.UserDao
import br.univesp.tcc.database.model.User
import br.univesp.tcc.extensions.tokenDataStore
import br.univesp.tcc.webclient.UserWebClient
import br.univesp.tcc.webclient.model.DTOCreateUser
import br.univesp.tcc.webclient.model.DTODeleteUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.single
import java.time.LocalDateTime
import android.content.Context
import br.univesp.tcc.extensions.dataStore
import br.univesp.tcc.webclient.model.DTOUpdateUser

private const val TAG = "UserRepository"

class UserRepository(
    private val userDao: UserDao,
    private val userWebClient: UserWebClient,
    private val ctx: Context
) {

    suspend fun getUsers(): List<User>? {

        val user = userDao.getAll().firstOrNull()

        Log.i(TAG, "getUsers - user: $user")

        return user
    }

    suspend fun  syncUsers() {

        Log.i(TAG, "syncUsers")

        val userToken = getToken()

        val userWeb = userWebClient.list(userToken)

        val users = userDao.getAll().firstOrNull() ?: listOf<User>()

        if(userWeb == null) return

        val updList : MutableList<User> = mutableListOf<User>()
        for(user in  userWeb)
        {
            val updUser = users.find { usr -> usr.id == user.id }
            if(updUser == null || updUser.updated < user.updated )
            {
                updList.add(user)
            }
        }

        Log.i(TAG, "syncUsers - users: $updList")
        if(updList.isNotEmpty()) userDao.save(updList)
    }

    fun getById(id : String): Flow<List<User>> {
        Log.i(TAG, "getById - id: $id")

        return userDao.getById(listOf(id))
    }

    suspend fun reloadAllUser(userToken: String) {
        Log.i(TAG, "updateUser")

        userWebClient.list(userToken)?.let { users ->
            userDao.saveAll(users)
        }
    }

    suspend fun insert(newUser: User) {

        Log.i(TAG, "insert - newUser: $newUser")
        val userToken = getToken()

        val userSearched = userDao.getByUserName(newUser.userName).firstOrNull()

        if (!userSearched.isNullOrEmpty()) return

        userDao.save(listOf(newUser))

        val userInserted = userWebClient.create(userToken, newUser)

        Log.i(TAG, "insert - userInserted: $userInserted")
    }

    suspend fun update(updateUser: User) {

        Log.i(TAG, "update - updateUser: $updateUser")
        val userToken = getToken()

        val userSearched = userDao.getById(listOf(updateUser.id) ).firstOrNull()

        if (userSearched.isNullOrEmpty()) return

        userDao.save(listOf(updateUser))

        val userUpdated = userWebClient.update(userToken, updateUser)

        Log.i(TAG, "update - userUpdated: $userUpdated")
    }

    suspend fun delete(idList: List<String>) {

        if (idList.isNotEmpty()) {

            val userToken = getToken()

            val user = userDao.getById(idList).firstOrNull()

            if (user.isNullOrEmpty()) return

            val data = DTODeleteUser(
                id = user.map { usr -> usr.id }
            )

            userDao.dellById(data.id)

            userWebClient.delete(userToken, data)
        }
    }

    private suspend fun getToken(): String{
        val preferences = ctx.dataStore.data.firstOrNull()

        if(preferences == null) return ""

        val userToken = preferences[tokenDataStore]

        if(userToken.isNullOrEmpty()) return ""

        return userToken
    }
}