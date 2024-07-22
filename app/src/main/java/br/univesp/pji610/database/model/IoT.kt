package br.univesp.pji610.database.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.util.UUID

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = GroupIoT::class,
            parentColumns = ["id"],
            childColumns = ["groupId"],
            onDelete = ForeignKey.CASCADE

        )
    ]
)
data class IoT(
    @PrimaryKey(autoGenerate = false)
    val id: String= UUID.randomUUID().toString(),
    val name: String,
    val createdAt: String,
    @ColumnInfo(index = true)
    val groupId: String,
)


data class IoT_GroupIoT(
    @Embedded val ioT: IoT,
    @Relation(parentColumn = "groupId", entityColumn = "id")
    val groupIoT: GroupIoT

)