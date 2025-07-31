package com.bridge.androidtechnicaltest.pupil.data.datasources.local

import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.LocalPupil
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.SyncStatus
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface PupilLocalDataSource {

    fun insertPupils(pupils: List<LocalPupil>): Completable
    fun upsertPupil(pupil: LocalPupil): Completable
    fun getPupils(): Single<List<LocalPupil>>
    fun getPupilsFlowable(): Flowable<List<LocalPupil>>
    fun getPupil(id: Int): Single<LocalPupil>
    fun deleteAllPupils(): Completable
    fun deleteByPupilId(pupilId: Long): Completable
    fun getUnsyncedPupils(): Single<List<LocalPupil>>
    fun getPupilsBySyncStatus(status: SyncStatus): Single<List<LocalPupil>>

}