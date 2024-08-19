package br.univesp.tcc.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.univesp.tcc.database.model.OrderAndItems
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderAndItemsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(orderAndItems: OrderAndItems):Long

    @Query("""SELECT * FROM OrderAndItems
                INNER JOIN Orders ON OrderAndItems.orderId = Orders.id
                WHERE Orders.userId = :userId 
                AND Orders.deleted = 0
    """)
    fun getAll(userId: String): Flow<List<OrderAndItems>>?
}