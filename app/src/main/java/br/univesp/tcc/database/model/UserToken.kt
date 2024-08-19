package br.univesp.tcc.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserToken (
    @PrimaryKey(autoGenerate = false)
    val userId: String,
    val isAdmin: Boolean,
    val userName: String,
    val token: String,
)


data class AuthenticatePost (
    val userName: String,
    val password: String,
)

