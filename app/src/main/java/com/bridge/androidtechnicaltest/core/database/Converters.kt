package com.bridge.androidtechnicaltest.core.database

import androidx.room.TypeConverter
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.SyncStatus
import java.util.Date

class Converters {
    @TypeConverter
    fun fromSyncStatus(status: SyncStatus): String {
        return status.name
    }

    @TypeConverter
    fun toSyncStatus(status: String): SyncStatus {
        return SyncStatus.valueOf(status)
    }

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time
}