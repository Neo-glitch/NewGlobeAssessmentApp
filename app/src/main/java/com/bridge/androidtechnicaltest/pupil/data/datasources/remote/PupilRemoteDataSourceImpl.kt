package com.bridge.androidtechnicaltest.pupil.data.datasources.remote

import com.bridge.androidtechnicaltest.core.network.NetworkHelper
import com.bridge.androidtechnicaltest.core.network.Resource
import com.bridge.androidtechnicaltest.pupil.data.datasources.remote.api.PupilApi
import com.bridge.androidtechnicaltest.pupil.data.datasources.remote.model.CreatePupilRequest
import com.bridge.androidtechnicaltest.pupil.data.datasources.remote.model.PupilsResponse
import com.bridge.androidtechnicaltest.pupil.data.datasources.remote.model.RemotePupil
import com.bridge.androidtechnicaltest.pupil.data.datasources.remote.model.UpdatePupilRequest
import io.reactivex.Single

class PupilRemoteDataSourceImpl(
    private val pupilApi: PupilApi
): PupilRemoteDataSource {

    override fun getPupils(page: Int): Single<Resource<PupilsResponse>> {
        return NetworkHelper.handleApiCallRx {
            pupilApi.getPupils(page)
        }
    }

    override fun getPupil(pupilId: Int): Single<Resource<RemotePupil>> {
        return NetworkHelper.handleApiCallRx {
            pupilApi.getPupil(pupilId)
        }
    }

    override fun createPupil(request: CreatePupilRequest): Single<Resource<RemotePupil>> {
        return NetworkHelper.handleApiCallRx {
            pupilApi.createPupil(request)
        }
    }

    override fun updatePupil(
        pupilId: Int,
        request: UpdatePupilRequest
    ): Single<Resource<RemotePupil>> {
        return NetworkHelper.handleApiCallRx {
            pupilApi.updatePupil(
                pupilId,
                request
            )
        }
    }

    override fun deletePupil(pupilId: Int): Single<Resource<Unit>> {
        return NetworkHelper.handleApiCallRx {
            pupilApi.deletePupil(pupilId)
        }
    }
}