package com.example.dbmonitor.data.repository

import com.example.dbmonitor.data.local.entities.DecibelRecord
import kotlinx.coroutines.flow.Flow

interface DecibelRepository {
    fun getDecibelStream(): Flow<Double>
    suspend fun saveReading(record: DecibelRecord)
    fun getHistory(): Flow<List<DecibelRecord>>
}
