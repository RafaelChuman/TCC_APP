package br.univesp.tcc.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.univesp.tcc.database.model.Orders
import kotlinx.coroutines.flow.Flow

@Dao
interface OrdersDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(order: Orders)

    @Query("""SELECT * FROM Orders
            WHERE userId = :userId 
            AND deleted = 0""")
    fun getAllByUser(userId: String): Flow<List<Orders>>?

    @Query("""SELECT * FROM Orders
            WHERE deleted = 0""")
    fun getAll(): Flow<List<Orders>>?

    @Query("UPDATE Orders set deleted = 1 WHERE id = :orderId")
    suspend fun remove(orderId: String)

    @Query("UPDATE Orders set statusOrder = 1 WHERE id = :orderId")
    suspend fun setDone(orderId: String)
}

//It's very important to multiple database statements
//@Transaction