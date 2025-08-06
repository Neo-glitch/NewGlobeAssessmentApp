package com.bridge.androidtechnicaltest.pupil.data.datasources.local

import androidx.paging.PagingSource
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.ClearedUnSyncedPupil
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.LocalPupil
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.PupilRemoteKey
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.SyncStatus

interface PupilLocalDataSource {
    suspend fun upsertPupil(pupil: LocalPupil)
    suspend fun getPupils(): List<LocalPupil>
    fun getPupilsPagingSource(): PagingSource<Int, LocalPupil>
    suspend fun getPupil(id: Int): LocalPupil
    suspend fun deleteAllPupils()

    suspend fun softDeletePupil(pupil: LocalPupil)
    suspend fun hardDeleteByPupilId(pupilId: Int)
    suspend fun getPupilsBySyncStatus(status: SyncStatus): List<LocalPupil>
    suspend fun updateLocalMediatorData(isRefresh: Boolean, localPupils: List<LocalPupil>, page: Int, endOfPagination: Boolean)

    // for remote keys
    suspend fun getRemoteKeyByPupilId(id: Int): PupilRemoteKey?

    // for cleared unsynced pupils
    suspend fun getClearedUnsyncedPupilsBySyncStatus(status: SyncStatus): List<ClearedUnSyncedPupil>
    suspend fun deleteClearedUnsyncedPupilById(id: Int)

    suspend fun deleteClearedUnsyncedOlderThan(cutoff: Long)

}