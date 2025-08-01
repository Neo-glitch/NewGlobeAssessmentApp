package com.bridge.androidtechnicaltest.pupil.data.datasources.local

import androidx.paging.PagingSource
import androidx.room.withTransaction
import com.bridge.androidtechnicaltest.core.database.AppDatabase
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.LocalPupil
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.PupilRemoteKeys
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.SyncStatus
import kotlinx.coroutines.flow.Flow

class PupilLocalDataSourceImpl(
//    private val pupilDao: PupilDao,
//    private val remoteKeysDao: PupilRemoteKeysDao,
    private val database: AppDatabase
) : PupilLocalDataSource {

    private val pupilDao = database.pupilDao
    private val remoteKeysDao = database.remoteKeysDao

    override suspend fun insertPupils(pupils: List<LocalPupil>) {
        pupilDao.insertPupils(pupils)
    }

    override suspend fun upsertPupil(pupil: LocalPupil) {
        pupilDao.upsertPupil(pupil)
    }

    override suspend fun getPupils(): List<LocalPupil> {
        return pupilDao.getPupils()
    }

    override fun getPupilsFlowable(): Flow<List<LocalPupil>> {
        return pupilDao.getPupilsFlowable()
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

    override suspend fun deletePupilsWithSyncedStatus() {
        pupilDao.deletePupilsWithSyncedStatus()
    }

    override suspend fun deleteByPupilId(pupilId: Long) {
        pupilDao.deleteByPupilId(pupilId)
    }

    override suspend fun getUnsyncedPupils(): List<LocalPupil> {
        return pupilDao.getUnsyncedPupils()
    }

    override suspend fun getPupilsBySyncStatus(status: SyncStatus): List<LocalPupil> {
        return pupilDao.getPupilsBySyncStatus(status)
    }

    override suspend fun getRemoteKeysByPublicId(id: Int): PupilRemoteKeys? {
        return remoteKeysDao.getRemoteKeysByPupilId(id)
    }

    override suspend fun updateLocalMediatorData(isRefresh: Boolean, localPupils: List<LocalPupil>, page: Int, endOfPagination: Boolean) {
        database.withTransaction {
            if (isRefresh) {
                remoteKeysDao.clearRemoteKeys()
                pupilDao.deletePupilsWithSyncedStatus()
            }

            pupilDao.insertPupils(localPupils)
            val keys = localPupils.map {
                PupilRemoteKeys(
                    pupilId = it.pupilId,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (endOfPagination) null else page + 1
                )
            }
            remoteKeysDao.insertAll(remoteKeys = keys)
        }
    }
}