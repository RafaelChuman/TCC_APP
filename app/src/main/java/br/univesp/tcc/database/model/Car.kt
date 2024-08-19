package br.univesp.tcc.database.model

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
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Car(
    @PrimaryKey(autoGenerate = false)
    val id: String= UUID.randomUUID().toString(),
    val brand: String,
    val model: String,
    val kind: String,
    val type: String,
    val plate: String,
    val yearOfFabrication: Int,
    val yearOfModel: Int,
    val color: String,
    @ColumnInfo(index = true)
    val createdAt: String,
    val userId: String,
    val deleted: Boolean,
    val updated: String,
)


//data class IoT_GroupIoT(
//    @Embedded val ioT: IoT,
//    @Relation(parentColumn = "groupId", entityColumn = "id")
//    val groupIoT: GroupIoT
//
//)