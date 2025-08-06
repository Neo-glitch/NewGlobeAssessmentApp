package com.bridge.androidtechnicaltest.pupil.domain.repository

import androidx.paging.PagingData
import com.bridge.androidtechnicaltest.core.domain.Resource
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.LocalPupil
import com.bridge.androidtechnicaltest.pupil.domain.model.PupilEntity
import kotlinx.coroutines.flow.Flow

interface PupilRepository {
    fun getOrFetchPupils() : Flow<PagingData<PupilEntity>>

    suspend fun getPupil(id: Int) : Resource<PupilEntity>

    suspend fun updatePupil(pupil: PupilEntity): Resource<Unit>

    suspend fun createPupil(pupil: PupilEntity): Resource<Unit>

    suspend fun softDeletePupil(pupil: PupilEntity): Resource<Unit>

    suspend fun hardDeletePupil(pupilEntity: PupilEntity): Resource<Unit>

    suspend fun syncPupils(): Resource<Unit>
}