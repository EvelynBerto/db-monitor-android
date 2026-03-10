package com.example.dbmonitor.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dbmonitor.data.local.entities.DecibelRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface DecibelDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: DecibelRecord)

    @Query("SELECT * FROM decibel_history ORDER BY timestamp DESC")
    fun getAllRecords(): Flow<List<DecibelRecord>>

    @Query("DELETE FROM decibel_history")
    suspend fun clearHistory()

    @Query("SELECT * FROM decibel_history WHERE value > :threshold")
    fun getHighNoiseRecords(threshold: Double): Flow<List<DecibelRecord>>
}
