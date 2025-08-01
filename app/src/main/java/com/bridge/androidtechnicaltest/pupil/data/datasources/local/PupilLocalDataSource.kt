package com.bridge.androidtechnicaltest.pupil.data.datasources.local

import androidx.paging.PagingSource
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.LocalPupil
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.PupilRemoteKeys
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.SyncStatus
import kotlinx.coroutines.flow.Flow

interface PupilLocalDataSource {

    suspend fun insertPupils(pupils: List<LocalPupil>)
    suspend fun upsertPupil(pupil: LocalPupil)
    suspend fun getPupils(): List<LocalPupil>
    fun getPupilsFlowable(): Flow<List<LocalPupil>>
    fun getPupilsPagingSource(): PagingSource<Int, LocalPupil>
    suspend fun getPupil(id: Int): LocalPupil
    suspend fun deleteAllPupils()
    suspend fun deletePupilsWithSyncedStatus()
    suspend fun deleteByPupilId(pupilId: Int)
    suspend fun getUnsyncedPupils(): List<LocalPupil>
    suspend fun getPupilsBySyncStatus(status: SyncStatus): List<LocalPupil>
    suspend fun updateLocalMediatorData(isRefresh: Boolean, localPupils: List<LocalPupil>, page: Int, endOfPagination: Boolean)

    // for remote keys
    suspend fun getRemoteKeysByPublicId(id: Int): PupilRemoteKeys?
}