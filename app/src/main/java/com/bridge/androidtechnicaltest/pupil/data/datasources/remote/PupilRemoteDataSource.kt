package com.bridge.androidtechnicaltest.pupil.data.datasources.remote

import com.bridge.androidtechnicaltest.core.network.Resource
import com.bridge.androidtechnicaltest.pupil.data.datasources.remote.model.CreatePupilRequest
import com.bridge.androidtechnicaltest.pupil.data.datasources.remote.model.PupilsResponse
import com.bridge.androidtechnicaltest.pupil.data.datasources.remote.model.RemotePupil
import com.bridge.androidtechnicaltest.pupil.data.datasources.remote.model.UpdatePupilRequest
import io.reactivex.Completable
import io.reactivex.Single

interface PupilRemoteDataSource {

    suspend fun getPupils(page: Int = 1): PupilsResponse
    suspend fun getPupil(pupilId: Int): RemotePupil
    suspend fun createPupil(request: CreatePupilRequest): RemotePupil
    suspend fun updatePupil(pupilId: Int, request: UpdatePupilRequest): RemotePupil
    suspend fun deletePupil(pupilId: Int): Unit

}