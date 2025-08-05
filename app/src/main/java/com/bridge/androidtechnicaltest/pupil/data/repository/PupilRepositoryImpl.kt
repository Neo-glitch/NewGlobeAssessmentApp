package com.bridge.androidtechnicaltest.pupil.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.bridge.androidtechnicaltest.core.network.NetworkHelper
import com.bridge.androidtechnicaltest.core.domain.Resource
import com.bridge.androidtechnicaltest.core.utils.GeneralExceptionHandler
import com.bridge.androidtechnicaltest.core.utils.K
import com.bridge.androidtechnicaltest.core.utils.isRemoteImage
import com.bridge.androidtechnicaltest.core.utils.orEmpty
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.PupilLocalDataSource
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.LocalPupil
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.SyncStatus
import com.bridge.androidtechnicaltest.pupil.data.datasources.remote.PupilRemoteDataSource
import com.bridge.androidtechnicaltest.pupil.data.datasources.remote.model.CreatePupilRequest
import com.bridge.androidtechnicaltest.pupil.data.datasources.remote.model.UpdatePupilRequest
import com.bridge.androidtechnicaltest.pupil.data.mapper.toEntity
import com.bridge.androidtechnicaltest.pupil.data.mapper.toModel
import com.bridge.androidtechnicaltest.pupil.data.paging.PupilRemoteMediator
import com.bridge.androidtechnicaltest.pupil.domain.repository.PupilRepository
import com.bridge.androidtechnicaltest.pupil.domain.model.PupilEntity
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.supervisorScope

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

    override suspend fun getPupil(id: Int): Resource<PupilEntity> {
        return try {
            return Resource.Success(localDataSource.getPupil(id).toEntity())
        } catch (t: Throwable) {
            Resource.Error(GeneralExceptionHandler.getErrorMessage(t))
        }
    }

    override suspend fun getUnsyncedPupils(): Resource<List<PupilEntity>> {
        return try {
            Resource.Success(localDataSource.getUnsyncedPupils().map { it.toEntity() })
        } catch (t: Throwable) {
            Resource.Error(GeneralExceptionHandler.getErrorMessage(t))
        }
    }

    override suspend fun updatePupil(pupil: PupilEntity): Resource<Unit> {
        return try {
            val localPupil = pupil.toModel().copy(
                syncStatus = SyncStatus.PENDING_UPDATE
            )

            localDataSource.upsertPupil(localPupil)
            Resource.Success(Unit)
        } catch (t: Throwable) {
            Resource.Error(GeneralExceptionHandler.getErrorMessage(t))
        }
    }

    override suspend fun createPupil(pupil: PupilEntity): Resource<Unit> {
        return try {
            val localPupil = pupil.toModel().copy(
                syncStatus = SyncStatus.PENDING_CREATE
            )
            localDataSource.upsertPupil(localPupil)
            Resource.Success(Unit)
        } catch (t: Throwable) {
            Resource.Error(GeneralExceptionHandler.getErrorMessage(t))
        }
    }

    override suspend fun softDeletePupil(pupil: PupilEntity): Resource<Unit> {
        return try {
            val localPupil = pupil.toModel()
            localDataSource.softDeletePupil(localPupil)
            Resource.Success(Unit)

        } catch (t: Throwable) {
            Resource.Error(GeneralExceptionHandler.getErrorMessage(t))
        }
    }

    override suspend fun hardDeletePupil(pupilEntity: PupilEntity): Resource<Unit> {
        return try {
            localDataSource.hardDeleteByPupilId(pupilEntity.id)
            Resource.Success(Unit)
        } catch (t: Throwable) {
            Resource.Error(GeneralExceptionHandler.getErrorMessage(t))
        }

    }

    override suspend fun syncPupils(): Resource<Unit> {
        var result: Resource<Unit> = Resource.Success(Unit)
        val pendingCreateSyncPupils =
            localDataSource.getPupilsBySyncStatus(SyncStatus.PENDING_CREATE)
        val pendingUpdateSyncPupils =
            localDataSource.getPupilsBySyncStatus(SyncStatus.PENDING_UPDATE)
        val pendingDeleteSyncPupils =
            localDataSource.getPupilsBySyncStatus(SyncStatus.PENDING_DELETE)

        supervisorScope {
            val responses = listOf(
                async { syncCreates(pendingCreateSyncPupils) },
                async { syncUpdates(pendingUpdateSyncPupils) },
                async { syncDeletes(pendingDeleteSyncPupils) }
            ).awaitAll()

            if (responses.any { it is Resource.Error }) {
                val errorMessage =
                    responses.filterIsInstance<Resource.Error>().firstOrNull()?.message
                result = Resource.Error(errorMessage.orEmpty)
            }
        }

        return result
    }

    private suspend fun syncCreates(pupils: List<LocalPupil>): Resource<Unit> {
        var result: Resource<Unit> = Resource.Success(Unit)
        pupils.forEach { pupil ->

            val response = NetworkHelper.handleApiCall {
                remoteDataSource.createPupil(
                    CreatePupilRequest(
                        name = pupil.name,
                        country = pupil.country,
                        image = pupil.image,
                        latitude = pupil.latitude,
                        longitude = pupil.longitude
                    )
                )
            }

            when (response) {
                is Resource.Error -> result = Resource.Error(response.message)
                is Resource.Success -> {
                    localDataSource.upsertPupil(
                        pupil.copy(
                            syncStatus = SyncStatus.SYNCED,
                            pupilId = response.data.pupilId,
                            image = response.data.image
                        )
                    )
                }
            }
        }

        return result
    }

    private suspend fun syncUpdates(pupils: List<LocalPupil>): Resource<Unit> {
        var result: Resource<Unit> = Resource.Success(Unit)

        pupils.forEach { pupil ->
            val image =
                if (pupil.image.isRemoteImage()) pupil.image else "http://lorempixel.com/640/480/sports?name=Eloy%20Vandervort"
            val response = NetworkHelper.handleApiCall {
                remoteDataSource.updatePupil(
                    pupil.pupilId,
                    UpdatePupilRequest(
                        pupilId = pupil.pupilId,
                        name = pupil.name,
                        country = pupil.country,
                        image = pupil.image,
                        latitude = pupil.latitude,
                        longitude = pupil.longitude
                    )
                )
            }

            when (response) {
                is Resource.Error -> result = Resource.Error(response.message)
                is Resource.Success -> {
                    localDataSource.upsertPupil(
                        pupil.copy(
                            image = response.data.image,
                            syncStatus = SyncStatus.SYNCED
                        )
                    )
                }
            }
        }

        return result
    }

    private suspend fun syncDeletes(pupils: List<LocalPupil>): Resource<Unit> {
        var result: Resource<Unit> = Resource.Success(Unit)

        pupils.forEach { pupil ->
            val response =
                NetworkHelper.handleApiCall { remoteDataSource.deletePupil(pupil.pupilId) }

            when (response) {
                is Resource.Error -> result = Resource.Error(response.message)
                is Resource.Success -> {
                    localDataSource.hardDeleteByPupilId(pupil.id)
                }
            }
        }

        return result
    }
}