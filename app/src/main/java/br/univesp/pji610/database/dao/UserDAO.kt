package br.univesp.pji610.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.univesp.pji610.database.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert
    suspend fun save(user: User)

    @Query(
        """
        SELECT * FROM User 
        WHERE userName = :userName
        AND password = :password"""
    )
    suspend fun authentication(
        userName: String,
        password: String
    ): User?

    @Query("SELECT * FROM User WHERE id = :userId")
    fun getById(userId: String): Flow<User>?
}