package com.example.dbmonitor.data.repository

import com.example.dbmonitor.data.local.DecibelDao
import com.example.dbmonitor.data.local.entities.DecibelRecord
import com.example.dbmonitor.data.sensor.AudioAnalyzer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

class DecibelRepositoryImpl(
    private val audioAnalyzer: AudioAnalyzer,
    private val decibelDao: DecibelDao,
) : DecibelRepository {

    override fun getDecibelStream(): Flow<Double> =
        audioAnalyzer.getDecibelStream()
            .onEach { db ->
                if (db > 85.0) {
                    decibelDao.insertRecord(DecibelRecord(value = db))
                }
            }

    override suspend fun saveReading(record: DecibelRecord) = decibelDao.insertRecord(record)

    override fun getHistory(): Flow<List<DecibelRecord>> = decibelDao.getAllRecords()
}
