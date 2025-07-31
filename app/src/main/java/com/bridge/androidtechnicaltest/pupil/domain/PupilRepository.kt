package com.bridge.androidtechnicaltest.pupil.domain

import com.bridge.androidtechnicaltest.pupil.data.datasources.remote.model.PupilsResponse
import io.reactivex.Single

interface PupilRepository {
    fun getOrFetchPupils() : Single<PupilsResponse>
}