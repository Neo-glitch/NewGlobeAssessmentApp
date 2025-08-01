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

    override suspend fun getPupils(page: Int): Resource<PupilsResponse> {
        return NetworkHelper.handleApiCall {
            pupilApi.getPupils(page)
        }
    }

    override suspend fun getPupil(pupilId: Int): Resource<RemotePupil> {
        return NetworkHelper.handleApiCall {
            pupilApi.getPupil(pupilId)
        }
    }

    override suspend fun createPupil(request: CreatePupilRequest): Resource<RemotePupil> {
        return NetworkHelper.handleApiCall {
            pupilApi.createPupil(request)
        }
    }

    override suspend fun updatePupil(
        pupilId: Int,
        request: UpdatePupilRequest
    ): Resource<RemotePupil> {
        return NetworkHelper.handleApiCall {
            pupilApi.updatePupil(
                pupilId,
                request
            )
        }
    }

    override suspend fun deletePupil(pupilId: Int): Resource<Unit> {
        return NetworkHelper.handleApiCall {
            pupilApi.deletePupil(pupilId)
        }
    }
}