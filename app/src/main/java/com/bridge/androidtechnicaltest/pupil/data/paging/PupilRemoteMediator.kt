package com.bridge.androidtechnicaltest.pupil.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.bridge.androidtechnicaltest.core.database.AppDatabase
import com.bridge.androidtechnicaltest.core.network.Resource
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.PupilLocalDataSource
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.LocalPupil
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.PupilRemoteKeys
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.SyncStatus
import com.bridge.androidtechnicaltest.pupil.data.datasources.remote.PupilRemoteDataSource
import com.bridge.androidtechnicaltest.pupil.data.datasources.remote.api.PupilApi
import com.bridge.androidtechnicaltest.pupil.data.mapper.toLocal

@OptIn(ExperimentalPagingApi::class)
class PupilRemoteMediator(
    private val localDataSource: PupilLocalDataSource,
    private val remoteDataSource: PupilRemoteDataSource
) : RemoteMediator<Int, LocalPupil>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, LocalPupil>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> 1
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val lastSyncedItem = state.pages
                    .lastOrNull { state -> state.data.any { it.syncStatus == SyncStatus.SYNCED } }
                    ?.data
                    ?.lastOrNull { it.syncStatus == SyncStatus.SYNCED }

                val remoteKey = lastSyncedItem?.let {
                    localDataSource.getRemoteKeysByPublicId(it.pupilId)
//                    database.remoteKeysDao.getRemoteKeysByPupilId(it.pupilId)
                }

                remoteKey?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = true)
            }
        }

        val response = remoteDataSource.getPupils(page)
        return when (response) {
            is Resource.Error -> MediatorResult.Error(Exception(response.message))
            is Resource.Success -> {
                val pupilsResponse = response.data

                val localPupils = pupilsResponse.items.orEmpty().map { remotePupil ->
                    remotePupil.toLocal()
                }
                val endOfPagination = pupilsResponse.pageNumber >= pupilsResponse.totalPages

                localDataSource.updateLocalMediatorData(
                    isRefresh = loadType == LoadType.REFRESH,
                    localPupils = localPupils,
                    page = page,
                    endOfPagination = endOfPagination
                )

                MediatorResult.Success(endOfPaginationReached = endOfPagination)
            }
        }
    }
}