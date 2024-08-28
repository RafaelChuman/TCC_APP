package br.univesp.tcc.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
<<<<<<<< HEAD:app/src/main/java/br/univesp/tcc/database/model/OrderAndItems.kt
            entity = Orders::class,
            parentColumns = ["id"],
            childColumns = ["orderId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Item::class,
========
            entity = User::class,
>>>>>>>> origin/master:app/src/main/java/br/univesp/tcc/database/model/Orders.kt
            parentColumns = ["id"],
            childColumns = ["itemId"],
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
<<<<<<<< HEAD:app/src/main/java/br/univesp/tcc/database/model/OrderAndItems.kt
data class OrderAndItems(
    @PrimaryKey(autoGenerate = false)
    val id: String= UUID.randomUUID().toString(),
    @ColumnInfo(index = true)
    val orderId: String,
    @ColumnInfo(index = true)
    val itemId: String,
    val quantity: Int,
    val price: Double,
    val discount: Double,
========
data class Orders(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val userId: String,
    val carId: String,
    val km: Int,
    val fuel: Double,
    val statusExecution: String,
    val statusOrder: Boolean,
    @ColumnInfo(index = true)
>>>>>>>> origin/master:app/src/main/java/br/univesp/tcc/database/model/Orders.kt
    val createdAt: String,
    val deleted: Boolean,
    val updated: String,
)