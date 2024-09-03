package br.univesp.tcc.database.model

import androidx.datastore.preferences.protobuf.Internal.BooleanList
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.util.UUID

@Entity
data class Item(
    @PrimaryKey(autoGenerate = false)
    val id: String= UUID.randomUUID().toString(),
    val type: String,
    @ColumnInfo(index = true)
    val name: String,
    @ColumnInfo(index = true)
    val createdAt: String,
    val deleted: Boolean,
    val updated: String,
)

//data class GroupIoT_IOT(
//    @Embedded val groupIoT: GroupIoT,
//    @Relation(parentColumn = "id", entityColumn = "groupId")
//    val ioTs: List<IoT>
//)