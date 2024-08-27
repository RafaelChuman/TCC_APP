package br.univesp.tcc.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.univesp.tcc.database.model.UserToken

@Dao
interface UserTokenDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(token: UserToken)

    @Query("""SELECT * FROM UserToken WHERE userId = :userId""")
    suspend fun getToken(userId: String): UserToken?

    @Query("""DELETE FROM UserToken WHERE userId = :userId""")
    suspend fun delToken(userId: String)

    @Query("""DELETE FROM UserToken""")
    suspend fun delAllToken()
}