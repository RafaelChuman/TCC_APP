package br.univesp.pji610.database.model

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
    val celular: Double,
    val telegram: String,
    val isAdmin: Boolean,
    val createdAt: String,
)