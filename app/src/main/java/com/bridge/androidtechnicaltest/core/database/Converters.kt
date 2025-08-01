package com.bridge.androidtechnicaltest.core.database

import androidx.room.TypeConverter
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.SyncStatus

class Converters {
    @TypeConverter
    fun fromSyncStatus(status: SyncStatus): String {
        return status.name
    }

    @TypeConverter
    fun toSyncStatus(status: String): SyncStatus {
        return SyncStatus.valueOf(status)
    }
}