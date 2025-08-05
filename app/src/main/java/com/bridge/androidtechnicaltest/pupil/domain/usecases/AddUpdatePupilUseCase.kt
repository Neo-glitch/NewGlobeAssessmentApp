package com.bridge.androidtechnicaltest.pupil.domain.usecases

import com.bridge.androidtechnicaltest.core.network.Resource
import com.bridge.androidtechnicaltest.core.utils.K
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.SyncStatus
import com.bridge.androidtechnicaltest.pupil.domain.repository.PupilRepository
import com.bridge.androidtechnicaltest.pupil.domain.model.PupilEntity

class AddUpdatePupilUseCase(
    private val repository: PupilRepository
) {
    suspend operator fun invoke(pupil: PupilEntity) : Resource<Unit> {
        return if (pupil.pupilId == K.TEMP_PUPIL_PUBLIC_ID)  {
            repository.createPupil(pupil)
        } else {
            repository.updatePupil(pupil)
        }
    }
}