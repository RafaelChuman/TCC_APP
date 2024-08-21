package br.univesp.tcc.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Car::class,
            parentColumns = ["id"],
            childColumns = ["carId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Orders(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    @ColumnInfo(index = true)
    val userId: String,
    @ColumnInfo(index = true)
    val carId: String,
    val km: Int,
    val fuel: Double,
    val statusExecution: String,
    val statusOrder: Boolean,
    @ColumnInfo(index = true)
    val createdAt: String,
    val deleted: Boolean,
    val updated: String,
)