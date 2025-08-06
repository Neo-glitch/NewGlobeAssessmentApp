package com.bridge.androidtechnicaltest.pupil.data.datasources.remote.api

import com.bridge.androidtechnicaltest.core.utils.K
import com.bridge.androidtechnicaltest.pupil.data.datasources.remote.model.CreatePupilRequest
import com.bridge.androidtechnicaltest.pupil.data.datasources.remote.model.PupilsResponse
import com.bridge.androidtechnicaltest.pupil.data.datasources.remote.model.RemotePupil
import com.bridge.androidtechnicaltest.pupil.data.datasources.remote.model.UpdatePupilRequest
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface PupilApi {
    @GET(K.GET_PUPILS)
    suspend fun getPupils(@Query("page") page: Int = 1): PupilsResponse

    @GET(K.GET_PUPIL)
    suspend fun getPupil(@Path("pupilId") pupilId: Int): RemotePupil

    @POST(K.CREATE_PUPIL)
    suspend fun createPupil(@Body request: CreatePupilRequest): RemotePupil

    @PUT(K.UPDATE_PUPIL)
    suspend fun updatePupil(@Path("pupilId") pupilId: Int, @Body request: UpdatePupilRequest): RemotePupil

    @DELETE(K.DELETE_PUPIL)
    suspend fun deletePupil(@Path("pupilId") pupilId: Int): Unit
}