package com.example.dbmonitor.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.dbmonitor.data.local.entities.DecibelRecord

/**
 * Created by Evelyn on 2026-03-10.
 *
 * Room Database for storing decibel records.
 */

internal const val DATA_BASE_NAME = "decibel_db"

@Database(entities = [DecibelRecord::class], version = 1, exportSchema = false)
abstract class DecibelDatabase : RoomDatabase() {
    abstract fun decibelDao(): DecibelDao

    companion object {
        @Volatile
        private var INSTANCE: DecibelDatabase? = null

        fun getDatabase(context: Context): DecibelDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DecibelDatabase::class.java,
                    DATA_BASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
