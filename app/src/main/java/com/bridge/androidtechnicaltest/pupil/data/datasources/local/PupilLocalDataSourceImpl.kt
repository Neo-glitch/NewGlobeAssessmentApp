package com.bridge.androidtechnicaltest.pupil.data.datasources.local

import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.LocalPupil
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.SyncStatus
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class PupilLocalDataSourceImpl(
    private val pupilDao: PupilDao
) : PupilLocalDataSource {

    override fun insertPupils(pupils: List<LocalPupil>): Completable {
        TODO("Not yet implemented")
    }

    override fun upsertPupil(pupil: LocalPupil): Completable {
        TODO("Not yet implemented")
    }

    override fun getPupils(): Single<List<LocalPupil>> {
        TODO("Not yet implemented")
    }

    override fun getPupilsFlowable(): Flowable<List<LocalPupil>> {
        TODO("Not yet implemented")
    }

    override fun getPupil(id: Int): Single<LocalPupil> {
        TODO("Not yet implemented")
    }

    override fun deleteAllPupils(): Completable {
        TODO("Not yet implemented")
    }

    override fun deleteByPupilId(pupilId: Long): Completable {
        TODO("Not yet implemented")
    }

    override fun getUnsyncedPupils(): Single<List<LocalPupil>> {
        TODO("Not yet implemented")
    }

    override fun getPupilsBySyncStatus(status: SyncStatus): Single<List<LocalPupil>> {
        TODO("Not yet implemented")
    }
}