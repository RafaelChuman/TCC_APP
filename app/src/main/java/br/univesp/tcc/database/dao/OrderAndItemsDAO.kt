package br.univesp.tcc.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import br.univesp.tcc.database.model.OrderAndItems
import br.univesp.tcc.webclient.model.DTOListOrderAndItemsByUser
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface OrderAndItemsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(orderAndItems: List<OrderAndItems>)

    @RewriteQueriesToDropUnusedColumns
    @Query("""SELECT OrderAndItems.id, OrderAndItems.orderId, OrderAndItems.type, OrderAndItems.name, OrderAndItems.unitMeasurement, OrderAndItems.quantity, OrderAndItems.price, OrderAndItems.discount, OrderAndItems.createdAt, OrderAndItems.deleted, OrderAndItems.updated, Orders.userId 
            FROM OrderAndItems
            INNER JOIN Orders ON OrderAndItems.orderId = Orders.orderId
            WHERE Orders.userId = :userId
                AND Orders.deleted = 0
                
    """)
    fun getByUserID(userId: String
    ): Flow<List<OrderAndItems>>?


    @RewriteQueriesToDropUnusedColumns
    @Query("""SELECT * 
            FROM OrderAndItems
            WHERE deleted = 0
    """)
    fun getAll(): Flow<List<OrderAndItems>>?
//    AND Orders.createdAt BETWEEN :dateBegin AND :dateEnd

    @RewriteQueriesToDropUnusedColumns
    @Query("""SELECT OrderAndItems.id, OrderAndItems.orderId, OrderAndItems.type, OrderAndItems.name, OrderAndItems.unitMeasurement, OrderAndItems.quantity, OrderAndItems.price, OrderAndItems.discount, OrderAndItems.createdAt, OrderAndItems.deleted, OrderAndItems.updated, Orders.userId 
            FROM OrderAndItems
            INNER JOIN Orders ON OrderAndItems.orderId = Orders.orderId
            WHERE OrderAndItems.orderId  = :orderId 
                AND Orders.deleted = 0
    """)
    fun getByOrderID(orderId: String): Flow<List<OrderAndItems>>?

    @RewriteQueriesToDropUnusedColumns
    @Query("""SELECT OrderAndItems.id, OrderAndItems.orderId, OrderAndItems.type, OrderAndItems.name, OrderAndItems.unitMeasurement, OrderAndItems.quantity, OrderAndItems.price, OrderAndItems.discount, OrderAndItems.createdAt, OrderAndItems.deleted, OrderAndItems.updated, Orders.userId 
            FROM OrderAndItems
            INNER JOIN Orders ON OrderAndItems.orderId = Orders.orderId
            WHERE OrderAndItems.id  = :id 
                AND Orders.deleted = 0
    """)
    fun getByID(id: List<String>): Flow<List<OrderAndItems>>?


    @Query("DELETE FROM OrderAndItems WHERE id IN (:id) ")
    suspend fun purgeByID(id: List<String>)


    @Query("DELETE FROM OrderAndItems WHERE orderId IN (:orderID) ")
    suspend fun purgeByOrderID(orderID: List<String>)

}

