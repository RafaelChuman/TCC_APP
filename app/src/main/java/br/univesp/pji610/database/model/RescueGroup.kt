package br.univesp.pji610.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = GroupIoT::class,
            parentColumns = ["id"],
            childColumns = ["groupId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RescueGroup(
    @PrimaryKey(autoGenerate = false)
    val id: String= UUID.randomUUID().toString(),
    @ColumnInfo(index = true)
    val userId: String,
    @ColumnInfo(index = true)
    val groupId: String,
    val createdAt: String,
)