package com.bridge.androidtechnicaltest.pupil.domain.usecases

import com.bridge.androidtechnicaltest.pupil.domain.repository.PupilRepository
import com.bridge.androidtechnicaltest.pupil.domain.model.PupilEntity

class DeletePupilUseCase(
    private val repository: PupilRepository
) {

    suspend operator fun invoke(pupilEntity: PupilEntity) {
        repository.deletePupil(pupilEntity)
    }
}