package br.univesp.tcc.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Update
import br.univesp.tcc.database.model.OrderAndItems
import br.univesp.tcc.database.model.Total
import br.univesp.tcc.webclient.model.DTOListOrderAndItemsByUser
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import java.time.Month

@Dao
interface OrderAndItemsDAO {

    @Insert()
    suspend fun insert(orderAndItems: List<OrderAndItems>)

    @Update()
    suspend fun update(orderAndItems: List<OrderAndItems>): Int

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


    @RewriteQueriesToDropUnusedColumns
    @Query("""SELECT CAST(STRFTIME('%m', OrderAndItems.createdAt) AS INTEGER) AS month, 
                     CAST(STRFTIME('%Y', OrderAndItems.createdAt) AS INTEGER) AS year, strftime('%Y', 'now')  as yearNow, 
                     SUM(((OrderAndItems.quantity * OrderAndItems.price) - OrderAndItems.discount)) as total
            FROM OrderAndItems
            INNER JOIN Orders ON OrderAndItems.orderId = Orders.orderId
            WHERE month  = :month 
                            AND year = 2023
                            AND Orders.deleted = 0            
            GROUP BY year, month
            
    """)
    suspend fun getProfitByMonth(month: Int): Total?

    @RewriteQueriesToDropUnusedColumns
    @Query("""SELECT    OrderAndItems.unitMeasurement,
                        CAST(STRFTIME('%m', OrderAndItems.createdAt) AS INTEGER) AS month, 
                        CAST(STRFTIME('%Y', OrderAndItems.createdAt) AS INTEGER) AS year, strftime('%Y', 'now')  as yearNow, 
                        SUM(((OrderAndItems.quantity * OrderAndItems.price) - OrderAndItems.discount)) as total
            FROM OrderAndItems
            INNER JOIN Orders ON OrderAndItems.orderId = Orders.orderId
            WHERE month  = :month 
                            AND year = 2023
                            AND unitMeasurement = 'MO'
                            AND Orders.deleted = 0            
            GROUP BY year, month
            
    """)
    suspend fun getLiquidProfitByMonth(month: Int): Total?


    @RewriteQueriesToDropUnusedColumns
    @Query("""SELECT    CAST(STRFTIME('%m', Orders.createdAt) AS INTEGER) AS month, 
                        CAST(STRFTIME('%Y', Orders.createdAt) AS INTEGER) AS year, strftime('%Y', 'now')  as yearNow, 
                        COUNT(Orders.orderId) as total
            FROM Orders
            WHERE month  = :month 
                            AND year = 2023
                            AND Orders.statusOrder = 1
                            AND Orders.deleted = 0            
            GROUP BY year, month
            
    """)
    suspend fun getOrdersNumberMonth(month: Int): Total?


    @RewriteQueriesToDropUnusedColumns
    @Query("""SELECT    CAST(STRFTIME('%m', Orders.createdAt) AS INTEGER) AS month, 
                        CAST(STRFTIME('%Y', Orders.createdAt) AS INTEGER) AS year, strftime('%Y', 'now')  as yearNow, 
                        COUNT(Orders.orderId) as total
            FROM Orders
            WHERE month  = :month 
                            AND year = 2023
                            AND Orders.statusOrder = 0
                            AND Orders.deleted = 0            
            GROUP BY year, month
            
    """)
    suspend fun getOrdersNotFinishedMonth(month: Int): Total?

}

