package com.bridge.androidtechnicaltest.pupil.data.datasources.local

import androidx.paging.PagingSource
import androidx.room.withTransaction
import com.bridge.androidtechnicaltest.core.database.AppDatabase
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.ClearedUnSyncedPupil
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.LocalPupil
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.PupilRemoteKey
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.SyncStatus
import com.bridge.androidtechnicaltest.pupil.data.mapper.toClearedUnsyncedPupil

class PupilLocalDataSourceImpl(
    private val database: AppDatabase
) : PupilLocalDataSource {

    private val pupilDao = database.pupilDao
    private val remoteKeysDao = database.remoteKeysDao
    private val clearedUnsyncedPupilDao = database.clearedUnsyncedPupilDao

    override suspend fun upsertPupil(pupil: LocalPupil) {
        pupilDao.upsertPupil(pupil)
    }

    override suspend fun getPupils(): List<LocalPupil> {
        return pupilDao.getPupils()
    }

    override fun getPupilsPagingSource(): PagingSource<Int, LocalPupil> {
        return pupilDao.getPupilsPagingSource()
    }

    override suspend fun getPupil(id: Int): LocalPupil {
        return pupilDao.getPupil(id)
    }

    override suspend fun deleteAllPupils() {
        return pupilDao.deleteAllPupils()
    }

    override suspend fun softDeletePupil(pupil: LocalPupil) {
        pupilDao.upsertPupil(pupil.copy(syncStatus = SyncStatus.PENDING_DELETE))
    }

    override suspend fun hardDeleteByPupilId(pupilId: Int) {
        database.withTransaction {
            pupilDao.deleteById(pupilId)
            remoteKeysDao.deleteById(pupilId)
        }
    }

    override suspend fun getPupilsBySyncStatus(status: SyncStatus): List<LocalPupil> {
        return pupilDao.getPupilsBySyncStatus(status)
    }

    override suspend fun getRemoteKeyByPupilId(id: Int): PupilRemoteKey? {
        return remoteKeysDao.getRemoteKeyByPupilId(id)
    }

    override suspend fun updateLocalMediatorData(
        isRefresh: Boolean,
        localPupils: List<LocalPupil>,
        page: Int,
        endOfPagination: Boolean
    ) {
        database.withTransaction {
            if (isRefresh) {
                val unsyncedPupils = pupilDao.getUnsyncedPupils()
                clearedUnsyncedPupilDao.insertClearedUnSyncedPupils(unsyncedPupils.map { it.toClearedUnsyncedPupil() })

                remoteKeysDao.clearRemoteKeys()
                pupilDao.deleteAllPupils()
            }


            val keys = localPupils.map {
                PupilRemoteKey(
                    pupilId = it.pupilId,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (endOfPagination) null else page + 1
                )
            }
            remoteKeysDao.insertAll(remoteKeys = keys)
            pupilDao.insertPupils(localPupils)
        }
    }

    override suspend fun getClearedUnsyncedPupilsBySyncStatus(status: SyncStatus): List<ClearedUnSyncedPupil> {
        return clearedUnsyncedPupilDao.getClearedUnsyncedPupilsBySyncStatus(status)
    }

    override suspend fun deleteClearedUnsyncedPupilById(id: Int) {
        clearedUnsyncedPupilDao.deleteById(id)
    }

    override suspend fun deleteClearedUnsyncedOlderThan(cutoff: Long) {
        clearedUnsyncedPupilDao.deleteOlderThan(cutoff)
    }
}