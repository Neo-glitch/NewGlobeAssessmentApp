package com.bridge.androidtechnicaltest.pupil.data.datasources.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Pupils")
class LocalPupil(
    @PrimaryKey
    @ColumnInfo(name = "pupil_id")
    val pupilId: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "country")
    val value: String,

    @ColumnInfo(name = "image")
    val image: String,

    @ColumnInfo(name = "latitude")
    val latitude: Double,

    @ColumnInfo(name = "longitude")
    val longitude: Double,

    @ColumnInfo(name = "sync_status")
    val syncStatus: SyncStatus
)