package com.bridge.androidtechnicaltest.pupil.domain.usecases

import com.bridge.androidtechnicaltest.core.network.Resource
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.SyncStatus
import com.bridge.androidtechnicaltest.pupil.domain.repository.PupilRepository
import com.bridge.androidtechnicaltest.pupil.domain.model.PupilEntity

class DeletePupilUseCase(
    private val repository: PupilRepository
) {

    suspend operator fun invoke(pupilEntity: PupilEntity): Resource<Unit> {
        return if (pupilEntity.syncStatus == SyncStatus.PENDING_CREATE) {
            repository.hardDeletePupil(pupilEntity)
        } else {
            repository.softDeletePupil(pupilEntity)
        }
    }
}