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
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
class Car() {

    @PrimaryKey(autoGenerate = false)
    var carId: String = UUID.randomUUID().toString()
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
    var createdAt: LocalDateTime
    var deleted: Boolean
    var updated: LocalDateTime

    @ColumnInfo(index = true)
    var userId: String

    init {
        carId = UUID.randomUUID().toString();
        brand = "";
        model = "";
        kind = "";
        type = "";
        plate = "";
        yearOfFabrication = 0;
        yearOfModel = 0;
        color = "";
        createdAt = LocalDateTime.now();
        deleted = false;
        updated = LocalDateTime.now();

        userId = "";
    }

    constructor(id: String, brand: String, model: String, kind: String, type: String, plate: String, yearOfFabrication: Int, yearOfModel: Int, color: String, createdAt: LocalDateTime, userId: String, deleted: Boolean, updated: LocalDateTime) : this() {
        this.carId =id
        this.brand = brand
        this.model = model
        this.kind = kind
        this.type = type
        this.plate = plate
        this.yearOfFabrication = yearOfFabrication
        this.yearOfModel =yearOfModel
        this.color = color
        this.createdAt = createdAt
        this.deleted = deleted
        this.updated = updated

        this.userId = userId
    }

    override fun toString(): String {
        return ("$carId - $model, $color, $plate, $userId")
    }
}



class CarUser(){
    var carId: String = UUID.randomUUID().toString()
    var brand: String
    var model: String
    var kind: String
    var type: String
    var plate: String
    var yearOfFabrication: Int
    var yearOfModel: Int
    var color: String
    var createdAt: LocalDateTime
    var deleted: Boolean
    var updated: LocalDateTime

    var userId: String
    var name: String
    var cellphone: String

    init {
        carId = UUID.randomUUID().toString();
        brand = "";
        model = "";
        kind = "";
        type = "";
        plate = "";
        yearOfFabrication = 0;
        yearOfModel = 0;
        color = "";
        createdAt = LocalDateTime.now();
        deleted = false;
        updated = LocalDateTime.now();

        userId = "";
        name = "";
        cellphone = "";

    }

    override fun toString(): String {
        return ("${carId} - ${model}, ${plate}")
    }
}

//data class IoT_GroupIoT(
//    @Embedded val ioT: IoT,
//    @Relation(parentColumn = "groupId", entityColumn = "id")
//    val groupIoT: GroupIoT
//
//)

