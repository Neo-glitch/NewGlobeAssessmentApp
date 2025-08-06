package com.bridge.androidtechnicaltest.pupil.data.datasources.remote.model

data class UpdatePupilRequest(
    val country: String,
    val image: String,
    val latitude: Double,
    val name: String,
    val longitude: Double,
    val pupilId: Int
)