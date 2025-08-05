package com.bridge.androidtechnicaltest.pupil.domain.model

import com.bridge.androidtechnicaltest.core.utils.K
import com.bridge.androidtechnicaltest.core.utils.generateRandomInt
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.SyncStatus

data class PupilEntity(
    val id: Int = 0,
    val pupilId: Int = K.TEMP_PUPIL_PUBLIC_ID,
    val name: String,
    val country: String,
    val image: String,
    val latitude: Double,
    val longitude: Double,
    val syncStatus: SyncStatus
)
