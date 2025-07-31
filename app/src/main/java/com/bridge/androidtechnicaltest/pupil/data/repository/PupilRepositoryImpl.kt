package com.bridge.androidtechnicaltest.pupil.data.repository

import com.bridge.androidtechnicaltest.core.database.AppDatabase
import com.bridge.androidtechnicaltest.pupil.data.datasources.remote.api.PupilApi
import com.bridge.androidtechnicaltest.pupil.data.datasources.remote.model.PupilsResponse
import com.bridge.androidtechnicaltest.pupil.domain.PupilRepository
import io.reactivex.Single
class PupilRepositoryImpl(val database: AppDatabase, val pupilApi: PupilApi): PupilRepository {

    override fun getOrFetchPupils(): Single<PupilsResponse> {
        // TODO("Continue with the implementation here")
        return Single.just(PupilsResponse())
    }
}