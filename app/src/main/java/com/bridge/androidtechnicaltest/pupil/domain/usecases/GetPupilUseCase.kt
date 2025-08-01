package com.bridge.androidtechnicaltest.pupil.domain.usecases

import com.bridge.androidtechnicaltest.core.network.Resource
import com.bridge.androidtechnicaltest.pupil.domain.repository.PupilRepository
import com.bridge.androidtechnicaltest.pupil.domain.model.PupilEntity

class GetPupilUseCase(
    private val repository: PupilRepository
) {
    suspend operator fun invoke(pupilId: Int) : Resource<PupilEntity> {
        return repository.getPupil(pupilId)
    }
}