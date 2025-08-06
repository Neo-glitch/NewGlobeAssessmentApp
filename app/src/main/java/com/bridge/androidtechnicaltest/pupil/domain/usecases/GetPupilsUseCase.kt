package com.bridge.androidtechnicaltest.pupil.domain.usecases

import androidx.paging.PagingData
import com.bridge.androidtechnicaltest.pupil.domain.repository.PupilRepository
import com.bridge.androidtechnicaltest.pupil.domain.model.PupilEntity
import kotlinx.coroutines.flow.Flow

class GetPupilsUseCase(
    private val repository: PupilRepository
) {
    operator fun invoke(): Flow<PagingData<PupilEntity>> = repository.getOrFetchPupils()
}