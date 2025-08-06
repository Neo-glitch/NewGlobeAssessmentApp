package com.bridge.androidtechnicaltest

import android.app.Application
import com.bridge.androidtechnicaltest.di.databaseModule
import com.bridge.androidtechnicaltest.di.locationModule
import com.bridge.androidtechnicaltest.di.networkModule
import com.bridge.androidtechnicaltest.pupil.di.pupilModule
import com.bridge.androidtechnicaltest.pupil.util.PupilSyncWorkManager
import com.bumptech.glide.Glide
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin
import org.koin.core.module.Module

class App : Application() {

    private val appComponent: MutableList<Module> = mutableListOf(
        networkModule, databaseModule, locationModule,
        pupilModule
    )

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(applicationContext)
            workManagerFactory()
            modules(appComponent)
        }

        PupilSyncWorkManager(this).enqueOneTimePupilSyncWork()
        PupilSyncWorkManager(this).enquePeriodicPupilSyncWork()
    }

    override fun onTrimMemory(level: Int) {
        clearGlideCache()
        super.onTrimMemory(level)
    }

    override fun onLowMemory() {
        clearGlideCache()
        super.onLowMemory()
    }

    private fun clearGlideCache() {
        Glide.get(this).clearMemory()

        Thread {
            Glide.get(this).clearDiskCache()
        }.start()
    }
}