package br.univesp.tcc.database.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.time.LocalDateTime
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
class Car() {

    @PrimaryKey(autoGenerate = false)
    var id: String = UUID.randomUUID().toString()
    var brand: String
    var model: String
    var kind: String
    var type: String
    @ColumnInfo(index = true)
    var plate: String
    var yearOfFabrication: Int
    var yearOfModel: Int
    var color: String
    @ColumnInfo(index = true)
    var createdAt: String
    @ColumnInfo(index = true)
    var userId: String
    var deleted: Boolean
    var updated: LocalDateTime

    init {
        id = UUID.randomUUID().toString();
        brand = "";
        model = "";
        kind = "";
        type = "";
        plate = "";
        yearOfFabrication = 0;
        yearOfModel = 0;
        color = "";
        createdAt = "";
        userId = "";
        deleted = false;
        updated = LocalDateTime.now();
    }

    constructor(
        id: String,
        brand: String,
        model: String,
        kind: String,
        type: String,
        plate: String,
        yearOfFabrication: Int,
        yearOfModel: Int,
        color: String,
        createdAt: String,
        userId: String,
        deleted: Boolean,
        updated: LocalDateTime
    ) : this() {
        this.id =id
        this.brand = brand
        this.model = model
        this.kind = kind
        this.type = type
        this.plate = plate
        this.yearOfFabrication = yearOfFabrication
        this.yearOfModel =yearOfModel
        this.color = color
        this.createdAt = createdAt
        this.userId = userId
        this.deleted = deleted
        this.updated = updated
    }
}


//data class IoT_GroupIoT(
//    @Embedded val ioT: IoT,
//    @Relation(parentColumn = "groupId", entityColumn = "id")
//    val groupIoT: GroupIoT
//
//)