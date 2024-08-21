package br.univesp.tcc.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.univesp.tcc.database.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(user: User):Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(user:  List<User>)

    @Query("""SELECT * FROM User 
                WHERE userName = :userName
                AND password = :password""")
    suspend fun authentication(
        userName: String,
        password: String
    ): User?

    @Query("""DELETE FROM User 
                WHERE id = :userId""")
    suspend fun dellById(
        userId: String
    )

    @Query("SELECT * FROM User WHERE id = :userId")
    fun getById(userId: String): Flow<User?>

    @Query("SELECT * FROM User")
    fun getAll(): Flow<List<User>?>
}