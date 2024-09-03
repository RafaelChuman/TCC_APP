package br.univesp.tcc.repository

import br.univesp.tcc.database.dao.UserDao
import br.univesp.tcc.database.model.User
import br.univesp.tcc.webclient.UserWebClient
import kotlinx.coroutines.flow.Flow

class UserRepository (private val dao: UserDao,
    private val userWebClient: UserWebClient){

    fun getUsers() : Flow<List<User>?>{
        return dao.getAll()
    }

    suspend fun updateUser(){
        userWebClient.list()?.let {
            users -> dao.saveAll(users)
        }
    }

}