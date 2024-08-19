package br.univesp.tcc.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Orders::class,
            parentColumns = ["id"],
            childColumns = ["orderId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Item::class,
            parentColumns = ["id"],
            childColumns = ["itemId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class OrderAndItems(
    @PrimaryKey(autoGenerate = false)
    val id: String= UUID.randomUUID().toString(),
    @ColumnInfo(index = true)
    val orderId: String,
    val itemId: String,
    val quantity: Int,
    val price: Double,
    val discount: Double,
    val createdAt: String,
)