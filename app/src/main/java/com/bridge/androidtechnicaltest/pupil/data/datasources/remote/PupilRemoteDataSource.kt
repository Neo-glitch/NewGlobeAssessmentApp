package com.bridge.androidtechnicaltest.pupil.data.datasources.remote

import com.bridge.androidtechnicaltest.core.network.Resource
import com.bridge.androidtechnicaltest.pupil.data.datasources.remote.model.CreatePupilRequest
import com.bridge.androidtechnicaltest.pupil.data.datasources.remote.model.PupilsResponse
import com.bridge.androidtechnicaltest.pupil.data.datasources.remote.model.RemotePupil
import com.bridge.androidtechnicaltest.pupil.data.datasources.remote.model.UpdatePupilRequest
import io.reactivex.Completable
import io.reactivex.Single

interface PupilRemoteDataSource {

    fun getPupils(page: Int = 1): Single<Resource<PupilsResponse>>
    fun getPupil(pupilId: Int): Single<Resource<RemotePupil>>
    fun createPupil(request: CreatePupilRequest): Single<Resource<RemotePupil>>
    fun updatePupil(pupilId: Int, request: UpdatePupilRequest): Single<Resource<RemotePupil>>
    fun deletePupil(pupilId: Int): Single<Resource<Unit>>

}