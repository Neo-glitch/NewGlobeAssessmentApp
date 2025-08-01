package com.bridge.androidtechnicaltest.pupil.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.bridge.androidtechnicaltest.core.network.Resource
import com.bridge.androidtechnicaltest.core.utils.K
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.PupilLocalDataSource
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.SyncStatus
import com.bridge.androidtechnicaltest.pupil.data.datasources.remote.PupilRemoteDataSource
import com.bridge.androidtechnicaltest.pupil.data.mapper.toEntity
import com.bridge.androidtechnicaltest.pupil.data.mapper.toModel
import com.bridge.androidtechnicaltest.pupil.data.paging.PupilRemoteMediator
import com.bridge.androidtechnicaltest.pupil.domain.PupilRepository
import com.bridge.androidtechnicaltest.pupil.domain.model.PupilEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PupilRepositoryImpl(
    val localDataSource: PupilLocalDataSource,
    val remoteDataSource: PupilRemoteDataSource
) : PupilRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getOrFetchPupils(): Flow<PagingData<PupilEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = K.PAGE_SIZE
            ),
            pagingSourceFactory = {
                localDataSource.getPupilsPagingSource()
            },
            remoteMediator = PupilRemoteMediator(
                remoteDataSource = remoteDataSource,
                localDataSource = localDataSource
            )
        ).flow.map { localPupils ->
            localPupils.map { localPupil ->
                localPupil.toEntity()
            }
        }
    }

    override suspend fun getPupil(pupilId: Int): Resource<PupilEntity> {
        return Resource.Success(localDataSource.getPupil(pupilId).toEntity())
    }

    override suspend fun updatePupil(pupil: PupilEntity) {
        val localPupil = pupil.toModel().copy(
            syncStatus = SyncStatus.PENDING_UPDATE
        )

        localDataSource.upsertPupil(localPupil)
    }

    override suspend fun createPupil(pupil: PupilEntity) {
        val localPupil = pupil.toModel().copy(
            syncStatus = SyncStatus.PENDING_CREATE
        )
        localDataSource.upsertPupil(localPupil)
    }

    override suspend fun deletePupil(pupil: PupilEntity) {
        val localPupil = pupil.toModel().copy(
            syncStatus = SyncStatus.PENDING_DELETE
        )
        localDataSource.upsertPupil(localPupil)

    }
}