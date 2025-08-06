package com.bridge.androidtechnicaltest.pupil.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.bridge.androidtechnicaltest.core.utils.GeneralExceptionHandler
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.PupilLocalDataSource
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.LocalPupil
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.PupilRemoteKey
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

            LoadType.PREPEND -> {
                val remoteKey = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKey?.prevKey
                prevKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
            }
            LoadType.APPEND -> {
                val remoteKey = getRemoteKeyForLastItem(state)
                val nextKey = remoteKey?.nextKey
                nextKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
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

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, LocalPupil>): PupilRemoteKey? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { pupil -> localDataSource.getRemoteKeyByPupilId(pupil.pupilId) } ?: getClosestValidRemoteKey(
                state, fromEnd = true
            )
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, LocalPupil>): PupilRemoteKey? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { pupil -> localDataSource.getRemoteKeyByPupilId(pupil.pupilId) } ?: getClosestValidRemoteKey(
                state,
                fromEnd = false
            )
    }

    private suspend fun getClosestValidRemoteKey(state: PagingState<Int, LocalPupil>, fromEnd: Boolean): PupilRemoteKey? {
        val allItems = state.pages.flatMap { it.data }

        val items = if (fromEnd) allItems.asReversed() else allItems

        for (item in items) {
            val key = localDataSource.getRemoteKeyByPupilId(item.pupilId)
            if (key != null) return key
        }
        return null
    }

}