package com.marwaeltayeb.photoweather

import android.app.Application
import com.marwaeltayeb.photoweather.di.networkModule
import com.marwaeltayeb.photoweather.di.repositoryModule
import com.marwaeltayeb.photoweather.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidLogger()
            androidContext(this@MainApplication)
            modules(listOf(repositoryModule, networkModule, viewModelModule))
        }
    }
}