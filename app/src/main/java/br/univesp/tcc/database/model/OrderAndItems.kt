package br.univesp.tcc.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.UUID

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Orders::class,
            parentColumns = ["orderId"],
            childColumns = ["orderId"],
            onDelete = ForeignKey.CASCADE
        )
//        ForeignKey(
//            entity = Item::class,
//            parentColumns = ["id"],
//            childColumns = ["itemId"],
//            onDelete = ForeignKey.CASCADE
//        )
    ]
)
class OrderAndItems() {
    @PrimaryKey(autoGenerate = false)
    var id: String = UUID.randomUUID().toString()

    @ColumnInfo(index = true)
    var orderId: String

    //@ColumnInfo(index = true)
    //val itemId: String,
    var type: String
    var name: String
    var unitMeasurement: String
    var quantity: Int
    var price: Double
    var discount: Double
    var createdAt: LocalDateTime
    var deleted: Boolean
    var updated: LocalDateTime

    init {
        id = UUID.randomUUID().toString()

        orderId = ""

        type = ""
        name = ""
        unitMeasurement = ""
        quantity = 0
        price = 0.0
        discount = 0.0
        createdAt =  LocalDateTime.now();
        deleted = false
        updated =  LocalDateTime.now();
    }

    override fun toString(): String {
        return ("$name , $price")
    }
}


class Total() {
    var month: String
    var year: String
    var yearNow: String
    var total: Double

    init {
        month = ""
        year = ""
        yearNow = ""
        total = 0.0
    }


    override fun toString(): String {
        return ("$month , $total")
    }
}