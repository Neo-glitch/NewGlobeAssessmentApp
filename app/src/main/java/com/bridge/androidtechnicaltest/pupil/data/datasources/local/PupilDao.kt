package com.bridge.androidtechnicaltest.pupil.data.datasources.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.LocalPupil
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.SyncStatus
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single


@Dao
interface PupilDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPupils(pupils: List<LocalPupil>): Completable

    @Upsert
    fun upsertPupil(pupil: LocalPupil): Completable

    @Query("SELECT * FROM Pupils WHERE sync_status != 'PENDING_DELETE' ORDER BY name ASC")
    fun getPupils(): Single<List<LocalPupil>>

    @Query("SELECT * FROM Pupils WHERE sync_status != 'PENDING_DELETE' ORDER BY name ASC")
    fun getPupilsFlowable(): Flowable<List<LocalPupil>>

//    @Query("SELECT * FROM Pupils ORDER BY name ASC")
//    fun getPupils(): Single<List<LocalPupil>>
//
//    @Query("SELECT * FROM Pupils ORDER BY name ASC")
//    fun getPupilsFlowable(): Flowable<List<LocalPupil>>

    @Query("SELECT * FROM Pupils WHERE pupil_id = :pupilId")
    fun getPupil(pupilId: Int): Single<LocalPupil>

    @Query("DELETE FROM Pupils")
    fun deleteAllPupils(): Completable

    @Query("DELETE FROM Pupils WHERE pupil_id = :pupilId")
    fun deleteByPupilId(pupilId: Long): Completable

    @Query("SELECT * FROM Pupils WHERE sync_status != 'SYNCED'")
    fun getUnsyncedPupils(): Single<List<LocalPupil>>

    @Query("SELECT * FROM Pupils WHERE sync_status = :status")
    fun getPupilsBySyncStatus(status: SyncStatus): Single<List<LocalPupil>>


}