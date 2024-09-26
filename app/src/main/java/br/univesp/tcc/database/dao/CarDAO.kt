package br.univesp.tcc.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import br.univesp.tcc.database.model.Car
import br.univesp.tcc.webclient.model.DTOListCarById
import kotlinx.coroutines.flow.Flow

@Dao
interface CarDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(car: List<Car>)

    @Query("UPDATE Car set deleted = 1 WHERE id IN (:id) ")
    suspend fun remove(id: List<String>)

    @Query("DELETE FROM Car WHERE id IN (:id) ")
    suspend fun purge(id: List<String>)

    @Query("""SELECT * FROM Car  """) // WHERE Car.deleted = 0 """)
    fun getAll(): Flow<List<Car>>

    @Query(
        """
        SELECT * 
        FROM Car
        WHERE plate IN (:plate)
        AND Car.deleted = 0"""
    )
    fun getByPlate(
        plate: List<String>,
    ): Flow<List<Car>>

    @Query(
        """
        SELECT * 
        FROM Car
        WHERE id IN (:id)
        AND Car.deleted = 0"""
    )
    fun getById(
        id: List<String>,
    ): Flow<List<Car>>

    @RewriteQueriesToDropUnusedColumns
    @Query(
        """
        SELECT * 
        FROM Car
        WHERE Car.userId = :userId
        AND Car.deleted = 0
        """
    )
    fun getByUser(userId: String): Flow<List<Car>>
}