package br.univesp.pji610.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.univesp.pji610.database.model.GroupIoT
import kotlinx.coroutines.flow.Flow


@Dao
interface GroupIoTDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(group: GroupIoT)

        @Query("""SELECT * FROM GroupIoT
        INNER JOIN RescueGroup ON RescueGroup.groupId = GroupIoT.id
        WHERE RescueGroup.userID = :userId""")
    fun getAllByUser(userId: String): Flow<List<GroupIoT>>

    @Query("""SELECT * FROM GroupIoT""")
    fun getAll(): Flow<List<GroupIoT>>


    @Query("""SELECT * FROM GroupIoT""")
    fun getName(): Flow< List<GroupIoT>>

    @Query("DELETE FROM GroupIoT WHERE id = :id")
    suspend fun remove(id: String)


    @Query(""" SELECT * FROM GroupIoT WHERE id = :groupIotId""")
    fun getById(
        groupIotId: String,
    ): Flow<GroupIoT?>?

}