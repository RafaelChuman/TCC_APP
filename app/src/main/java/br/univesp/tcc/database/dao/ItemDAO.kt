package br.univesp.tcc.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.univesp.tcc.database.model.Item
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(rescueGroup: Item):Long

    @Query("""SELECT * FROM Item WHERE id = :itemId AND deleted = 0 """)
    fun getById(itemId: String): Flow<Item>?

    @Query("""SELECT * FROM Item WHERE deleted = 0""")
    fun getAll(): Flow<List<Item>>?

    @Query("""SELECT * FROM Item WHERE (name like :filter OR type like :filter) AND deleted = 0 """)
    fun getAllByAnyFilter(filter: String): Flow<List<Item>>?

    @Query(""" UPDATE Item set deleted = 1 WHERE id = :itemId """)
    suspend fun remove(itemId: String)

    @Query(""" DELETE FROM Item WHERE id = :itemId """)
    suspend fun purge(itemId: String)
}