package br.univesp.tcc.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.univesp.tcc.database.model.Car
import kotlinx.coroutines.flow.Flow

@Dao
interface CarDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(car: Car):Long

    @Query(
        """
        SELECT * 
        FROM Car
        INNER JOIN User ON User.id = Car.userId
        WHERE Car.userId = :userId
        AND Car.deleted = 0
        """
    )
    fun getAllByUser(userId: String): Flow<List<Car>>?

    @Query(
        """
        SELECT * 
        FROM Car
        WHERE id = :carId
        AND Car.deleted = 0"""
    )
    fun getById(
        carId: String,
    ): Flow<Car>?

    @Query("UPDATE Car set deleted = 1 WHERE id = :carId")
    suspend fun remove(carId: String)

    @Query("""SELECT * FROM Car WHERE Car.deleted = 0 """)
    fun getAll(): Flow<List<Car>>?
}