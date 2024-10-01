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
class Orders(){
    @PrimaryKey(autoGenerate = false)
    var id: String
    @ColumnInfo(index = true)
    var userId: String
    @ColumnInfo(index = true)
    var carId: String
    var km: Int
    var fuel: Double
    var statusExecution: String
    var statusOrder: Boolean
    @ColumnInfo(index = true)
    var createdAt: LocalDateTime
    var deleted: Boolean
    var updated: LocalDateTime

    init {
        id = UUID.randomUUID().toString();
        km = 0;
        fuel = 0.0;
        statusExecution = "";
        statusOrder = false;
        createdAt = LocalDateTime.now();
        deleted = false;
        updated = LocalDateTime.now();

        userId = "";
        carId = "";
    }

    override fun toString(): String {
    return ("$id - $userId, $carId")
    }
}
