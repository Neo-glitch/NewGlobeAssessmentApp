package com.bridge.androidtechnicaltest.core.database

import android.content.Context
import androidx.room.Room

object DatabaseFactory {

    fun getDBInstance(context: Context) =
        Room.databaseBuilder(context, AppDatabase::class.java, "TechnicalTestDb")
            .fallbackToDestructiveMigration()
            .build()
}