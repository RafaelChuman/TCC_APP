package br.univesp.pji610.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.univesp.pji610.database.model.MonitorIoT

@Dao
interface MonitorIotDAO {

    @Insert
    suspend fun save(monitorIoT: MonitorIoT)

    @Query("""SELECT * FROM MonitorIoT""")
    suspend fun getAll(): MonitorIoT?

}