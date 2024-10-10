package br.univesp.tcc.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.UUID

@Entity
class User() {

    @PrimaryKey(autoGenerate = false)
    var userId: String = UUID.randomUUID().toString()
    var name: String
    var userName: String
    var password: String
    var imgPath: String
    var email: String
    var cellphone: String
    var telegram: String
    var isAdmin: Boolean
    @ColumnInfo(index = true)
    var createdAt: LocalDateTime
    var deleted: Boolean
    var updated: LocalDateTime

    init {;
        name = "";
        userName = "";
        password = "";
        imgPath = "";
        email = "";
        cellphone = "";
        telegram = "";
        isAdmin = false;
        createdAt = LocalDateTime.now();
        deleted = false;
        updated = LocalDateTime.now();
    }

    constructor(
        id: String,
        name: String,
        userName: String,
        password: String,
        imgPath: String,
        email: String,
        cellphone: String,
        telegram: String,
        isAdmin: Boolean,
        createdAt: LocalDateTime,
        deleted: Boolean,
        updated: LocalDateTime,
    ) : this() {
        this.userId = id
        this.name = name
        this.userName = userName
        this.password = password
        this.imgPath = imgPath
        this.email = email
        this.cellphone = cellphone
        this.telegram = telegram
        this.isAdmin = isAdmin
        this.createdAt = createdAt
        this.deleted = deleted
        this.updated = updated
    }

    constructor(
        name: String,
        userName: String,
        password: String,
        imgPath: String,
        email: String,
        cellphone: String,
        telegram: String,
        isAdmin: Boolean,
        createdAt: LocalDateTime,
        deleted: Boolean,
        updated: LocalDateTime,
    ) : this() {
        this.name = name
        this.userName = userName
        this.password = password
        this.imgPath = imgPath
        this.email = email
        this.cellphone = cellphone
        this.telegram = telegram
        this.isAdmin = isAdmin
        this.createdAt = createdAt
        this.deleted = deleted
        this.updated = updated
    }

    override fun toString(): String {
        return ("$userId - $name")
    }
}
