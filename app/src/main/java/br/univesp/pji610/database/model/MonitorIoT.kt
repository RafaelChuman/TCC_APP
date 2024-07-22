package br.univesp.pji610.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class MonitorIoT(
    @PrimaryKey(autoGenerate = false)
    val id: String= UUID.randomUUID().toString(),
    val name: String,
    val temperature: Double,
    val humidity: Double,
    val noBreak: Double,
    val userId: String,
    val ioTId: String,
    val rescueGroupId: String,
    val createdAt: String,
)