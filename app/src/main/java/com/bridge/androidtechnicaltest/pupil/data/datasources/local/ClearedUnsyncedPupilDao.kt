package com.bridge.androidtechnicaltest.pupil.data.datasources.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.ClearedUnSyncedPupil
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.SyncStatus


@Dao
interface ClearedUnsyncedPupilDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClearedUnSyncedPupils(pupils: List<ClearedUnSyncedPupil>)

    @Upsert
    suspend fun upsertClearedUnsyncedPupil(pupil: ClearedUnSyncedPupil)

    @Query("SELECT * FROM cleared_unsynced_pupils")
    suspend fun getClearedUnsyncedPupils(): List<ClearedUnSyncedPupil>

    @Query("SELECT * FROM cleared_unsynced_pupils WHERE sync_status = :status")
    suspend fun getClearedUnsyncedPupilsBySyncStatus(status: SyncStatus): List<ClearedUnSyncedPupil>

    @Query("SELECT * FROM cleared_unsynced_pupils WHERE id = :id")
    suspend fun getClearedUnsyncedPupil(id: Int): ClearedUnSyncedPupil

    @Query("DELETE FROM cleared_unsynced_pupils")
    suspend fun deleteAll()

    @Query("DELETE FROM cleared_unsynced_pupils WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM cleared_unsynced_pupils WHERE last_modified < :cutoff")
    suspend fun deleteOlderThan(cutoff: Long)
}