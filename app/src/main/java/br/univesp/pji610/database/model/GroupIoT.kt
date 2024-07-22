package br.univesp.pji610.database.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.util.UUID

@Entity
data class GroupIoT(
    @PrimaryKey(autoGenerate = false)
    val id: String= UUID.randomUUID().toString(),
    val name: String,
    val temperature: Double,
    val humidity: Double,
    val noBreak: Double,
    val createdAt: String,
)

data class GroupIoT_IOT(
    @Embedded val groupIoT: GroupIoT,
    @Relation(parentColumn = "id", entityColumn = "groupId")
    val ioTs: List<IoT>
)