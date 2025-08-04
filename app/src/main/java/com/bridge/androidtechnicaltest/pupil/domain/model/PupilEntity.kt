package com.bridge.androidtechnicaltest.pupil.domain.model

import com.bridge.androidtechnicaltest.core.utils.generateRandomInt
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.SyncStatus

data class PupilEntity(
    val pupilId: Int = generateRandomInt(),
    val name: String,
    val country: String,
    val image: String,
    val latitude: Double,
    val longitude: Double,
    val syncStatus: SyncStatus
)
