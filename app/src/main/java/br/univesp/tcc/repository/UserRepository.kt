package br.univesp.tcc.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import br.univesp.tcc.database.dao.UserDao
import br.univesp.tcc.database.model.Car
import br.univesp.tcc.database.model.User
import br.univesp.tcc.extensions.tokenDataStore
import br.univesp.tcc.extensions.userIdDataStore
import br.univesp.tcc.webclient.UserWebClient
import br.univesp.tcc.webclient.model.DTOCreateCar
import br.univesp.tcc.webclient.model.DTOCreateUser
import br.univesp.tcc.webclient.model.DTODeleteUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.single
import java.time.LocalDateTime
import android.content.Context
import br.univesp.tcc.extensions.dataStore

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

        val preferences = ctx.dataStore.data.firstOrNull()
        val userToken = preferences?.get(tokenDataStore) ?: return

        val userWeb = userWebClient.list(userToken)

        val users = userDao.getAll().firstOrNull() ?: return

        if(userWeb == null) return

        val updList : MutableList<User> = mutableListOf<User>()
        for(user in  userWeb)
        {
            val updUser = users.find { usr -> usr.id == user.id }
            if(updUser == null || updUser.updated > user.updated )
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

    suspend fun updateUser(userToken: String) {
        Log.i(TAG, "updateUser")

        userWebClient.list(userToken)?.let { users ->
            userDao.saveAll(users)
        }
    }

    suspend fun insert(newUser: DTOCreateUser) {

        Log.i(TAG, "insert - newUser: $newUser")

        val createdAt = LocalDateTime.now()

        val userSearched = userDao.getByUserName(newUser.userName).single()

        if (userSearched.isEmpty()) return

        val user = User(
            userName = newUser.userName,
            name = newUser.name,
            password = newUser.password,
            imgPath = newUser.imgPath,
            email = newUser.email,
            cellphone = newUser.cellphone,
            telegram = newUser.telegram,
            isAdmin = newUser.isAdmin,
            createdAt = createdAt,
            deleted = false,
            updated = createdAt
        )

        userDao.save(listOf(user))

        val userInserted = userWebClient.create(user)

        Log.i(TAG, "insert - userInserted: $userInserted")
    }

    suspend fun delete(idList: List<String>) {

        if (idList.isNotEmpty()) {

            val user = userDao.getById(idList).single()

            if (user.isEmpty()) return

            val data = DTODeleteUser(
                id = user.map { usr -> usr.id }
            )

            userDao.dellById(data.id)

            userWebClient.delete(data)
        }
    }
}