package br.univesp.tcc.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.univesp.tcc.database.model.Orders
import br.univesp.tcc.database.model.OrdersCarUser
import kotlinx.coroutines.flow.Flow

@Dao
interface OrdersDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(order: List<Orders>)

    @Query("""SELECT * FROM Orders
            WHERE userId = :userId 
            AND deleted = 0""")
    fun getAllByUser(userId: String): Flow<List<Orders>>?

    @Query("""SELECT * FROM Orders
            WHERE orderId IN(:id)  
            AND deleted = 0""")
    fun getById(id: List<String>): Flow<List<Orders>>

    @Query("""SELECT Orders.orderId AS orderId, Orders.userId AS userId, Orders.carId AS carId, Orders.km AS km, Orders.fuel AS fuel,
            Orders.statusExecution AS statusExecution, Orders.statusOrder AS statusOrder, Orders.createdAt AS createdAt, 
            Orders.deleted AS deleted, Orders.updated AS updated, 
            Car.color AS color, Car.model AS model, Car.plate AS plate, User.name AS name, User.cellphone AS cellphone
            FROM Orders
            INNER JOIN Car ON Car.carId = Orders.carId
            INNER JOIN User ON User.userId = Orders.userId
            WHERE Orders.deleted = 0""")
    fun getAll(): Flow<List<OrdersCarUser>>

    @Query("UPDATE Orders set deleted = 1 WHERE orderId IN (:orderId)")
    suspend fun remove(orderId: List<String>)

    @Query("DELETE FROM Orders WHERE orderId IN (:orderId)")
    suspend fun purge(orderId: List<String>)

    @Query("UPDATE Orders set statusOrder = 1 WHERE orderId = :orderId")
    suspend fun setDone(orderId: String)



}

//It's very important to multiple database statements
//@Transaction