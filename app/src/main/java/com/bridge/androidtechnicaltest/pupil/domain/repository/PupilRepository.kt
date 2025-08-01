package com.bridge.androidtechnicaltest.pupil.domain.repository

import androidx.paging.PagingData
import com.bridge.androidtechnicaltest.core.network.Resource
import com.bridge.androidtechnicaltest.pupil.domain.model.PupilEntity
import kotlinx.coroutines.flow.Flow

interface PupilRepository {
    fun getOrFetchPupils() : Flow<PagingData<PupilEntity>>

    suspend fun getPupil(pupilId: Int) : Resource<PupilEntity>

    suspend fun updatePupil(pupil: PupilEntity)

    suspend fun createPupil(pupil: PupilEntity)

    suspend fun deletePupil(pupil: PupilEntity)

    suspend fun syncPupils(): Resource<Unit>
}