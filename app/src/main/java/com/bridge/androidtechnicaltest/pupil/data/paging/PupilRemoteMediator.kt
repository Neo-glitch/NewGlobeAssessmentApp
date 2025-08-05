package com.bridge.androidtechnicaltest.pupil.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.bridge.androidtechnicaltest.core.domain.Resource
import com.bridge.androidtechnicaltest.core.utils.GeneralExceptionHandler
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.PupilLocalDataSource
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.LocalPupil
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.SyncStatus
import com.bridge.androidtechnicaltest.pupil.data.datasources.remote.PupilRemoteDataSource
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
                }

                remoteKey?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = true)
            }
        }

        return try {
            val pupilsResponse = remoteDataSource.getPupils(page)

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
        } catch (t: Throwable) {
            val errorMessage = GeneralExceptionHandler.getErrorMessage(t)
            MediatorResult.Error(Exception(errorMessage))
        }
    }
}