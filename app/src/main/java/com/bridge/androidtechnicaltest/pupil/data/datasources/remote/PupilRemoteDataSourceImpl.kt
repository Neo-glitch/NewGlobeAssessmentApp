package com.bridge.androidtechnicaltest.pupil.data.datasources.remote

import com.bridge.androidtechnicaltest.core.network.NetworkHelper
import com.bridge.androidtechnicaltest.core.network.Resource
import com.bridge.androidtechnicaltest.pupil.data.datasources.remote.api.PupilApi
import com.bridge.androidtechnicaltest.pupil.data.datasources.remote.model.CreatePupilRequest
import com.bridge.androidtechnicaltest.pupil.data.datasources.remote.model.PupilsResponse
import com.bridge.androidtechnicaltest.pupil.data.datasources.remote.model.RemotePupil
import com.bridge.androidtechnicaltest.pupil.data.datasources.remote.model.UpdatePupilRequest

class PupilRemoteDataSourceImpl(
    private val pupilApi: PupilApi
): PupilRemoteDataSource {

    override suspend fun getPupils(page: Int): PupilsResponse {
        return pupilApi.getPupils(page)
    }

    override suspend fun getPupil(pupilId: Int): RemotePupil {
        return pupilApi.getPupil(pupilId)
    }

    override suspend fun createPupil(request: CreatePupilRequest): RemotePupil {
        return pupilApi.createPupil(request)
    }

    override suspend fun updatePupil(
        pupilId: Int,
        request: UpdatePupilRequest
    ): RemotePupil {
        return pupilApi.updatePupil(
            pupilId,
            request
        )
    }

    override suspend fun deletePupil(pupilId: Int): Unit {
        return pupilApi.deletePupil(pupilId)
    }
}