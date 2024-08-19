package br.univesp.tcc.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID


@Entity
data class User(
    @PrimaryKey(autoGenerate = false)
    val id: String= UUID.randomUUID().toString(),
    val name: String,
    val userName: String,
    val password: String,
    val imgPath: String,
    val email: String,
    val cellphone: String,
    val telegram: String,
    val isAdmin: Boolean,
    @ColumnInfo(index = true)
    val createdAt: String,
    val deleted: Boolean,
    val updated: String,
)