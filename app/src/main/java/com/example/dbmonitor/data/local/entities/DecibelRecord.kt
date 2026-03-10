package com.example.dbmonitor.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "decibel_history")
data class DecibelRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val value: Double,
    val timestamp: Long = System.currentTimeMillis(),
)
