package com.bridge.androidtechnicaltest.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.LocalPupil
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.PupilDao
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.PupilRemoteKeysDao
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.PupilRemoteKeys

@Database(entities = [LocalPupil::class, PupilRemoteKeys::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val pupilDao: PupilDao

    abstract val remoteKeysDao: PupilRemoteKeysDao
}