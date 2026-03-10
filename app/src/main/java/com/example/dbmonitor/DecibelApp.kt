package com.example.dbmonitor

import android.app.Application
import com.example.dbmonitor.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class DbMonitor : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@DbMonitor)
            modules(appModule)
        }
    }
}
