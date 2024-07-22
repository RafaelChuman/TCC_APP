package br.univesp.pji610.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.univesp.pji610.database.model.RescueGroup

@Dao
interface RescueGroupDAO {

    @Insert
    suspend fun save(rescueGroup: RescueGroup):Long

    @Query("""SELECT * FROM RescueGroup """)
    suspend fun getAll(): RescueGroup?
}