package com.bridge.androidtechnicaltest

import android.app.Application
import com.bridge.androidtechnicaltest.di.databaseModule
import com.bridge.androidtechnicaltest.di.networkModule
import com.bridge.androidtechnicaltest.pupil.di.pupilModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module

class App : Application() {

    private val appComponent: MutableList<Module> = mutableListOf(
        networkModule, databaseModule,
        pupilModule
    )

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(applicationContext)
            modules(appComponent)
        }
    }
}