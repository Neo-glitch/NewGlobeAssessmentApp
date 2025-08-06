package com.bridge.androidtechnicaltest.pupil.data.datasources.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pupil_remote_keys")
data class PupilRemoteKey(
    @PrimaryKey val pupilId: Int,
    val prevKey: Int?,
    val nextKey: Int?
)