package com.bridge.androidtechnicaltest.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.LocalPupil
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.PupilDao
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.PupilRemoteKeysDao

@Database(entities = [LocalPupil::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract val pupilDao: PupilDao

    abstract val remoteKeysDao: PupilRemoteKeysDao
}