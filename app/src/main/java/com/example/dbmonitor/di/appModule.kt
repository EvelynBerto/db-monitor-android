package com.example.dbmonitor.di

import androidx.room.Room
import com.example.dbmonitor.data.local.DATA_BASE_NAME
import com.example.dbmonitor.data.local.DecibelDatabase
import com.example.dbmonitor.data.repository.DecibelRepository
import com.example.dbmonitor.data.repository.DecibelRepositoryImpl
import com.example.dbmonitor.data.sensor.AudioAnalyzer
import com.example.dbmonitor.ui.viewmodel.MonitorViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { 
        Room.databaseBuilder(
            androidContext(),
            DecibelDatabase::class.java,
            DATA_BASE_NAME
        ).build() 
    }

    // DAO
    single { get<DecibelDatabase>().decibelDao() }

    single { AudioAnalyzer() }

    single<DecibelRepository> {
        DecibelRepositoryImpl(
            audioAnalyzer = get(),
            decibelDao = get(),
        )
    }

    viewModel { MonitorViewModel(get()) }

}
