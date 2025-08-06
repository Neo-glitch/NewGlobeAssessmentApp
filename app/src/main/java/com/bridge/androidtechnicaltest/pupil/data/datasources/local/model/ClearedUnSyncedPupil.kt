package com.bridge.androidtechnicaltest.pupil.data.datasources.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bridge.androidtechnicaltest.core.utils.K

@Entity(tableName = "cleared_unsynced_pupils")
data class ClearedUnSyncedPupil(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "pupil_id")
    val pupilId: Int = K.TEMP_PUPIL_PUBLIC_ID,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "country")
    val country: String,

    @ColumnInfo(name = "image")
    val image: String,

    @ColumnInfo(name = "latitude")
    val latitude: Double,

    @ColumnInfo(name = "longitude")
    val longitude: Double,

    @ColumnInfo(name = "last_modified")
    val lastModified: Long,

    @ColumnInfo(name = "sync_status")
    val syncStatus: SyncStatus
)
