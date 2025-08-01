package com.bridge.androidtechnicaltest.pupil.domain.usecases

import com.bridge.androidtechnicaltest.pupil.domain.repository.PupilRepository
import com.bridge.androidtechnicaltest.pupil.domain.model.PupilEntity

class AddUpdatePupilUseCase(
    private val repository: PupilRepository
) {
    suspend operator fun invoke(pupil: PupilEntity, isPupilCreation: Boolean) {
        if (isPupilCreation) {
            repository.createPupil(pupil)
        } else {
            repository.updatePupil(pupil)
        }
    }
}