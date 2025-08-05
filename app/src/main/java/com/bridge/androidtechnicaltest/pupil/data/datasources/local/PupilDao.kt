package com.bridge.androidtechnicaltest.pupil.data.datasources.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.LocalPupil
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.SyncStatus
import kotlinx.coroutines.flow.Flow


@Dao
interface PupilDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPupils(pupils: List<LocalPupil>)

    @Upsert
    suspend fun upsertPupil(pupil: LocalPupil)

    @Query("SELECT * FROM Pupils WHERE sync_status != 'PENDING_DELETE' ORDER BY name ASC")
    suspend fun getPupils(): List<LocalPupil>

    @Query("SELECT * FROM Pupils WHERE sync_status != 'PENDING_DELETE' ORDER BY name ASC")
    fun getPupilsFlowable(): Flow<List<LocalPupil>>

    @Query("SELECT * FROM Pupils WHERE sync_status != 'PENDING_DELETE' ORDER BY name ASC")
    fun getPupilsPagingSource(): PagingSource<Int, LocalPupil>

    @Query("SELECT * FROM Pupils WHERE id = :id")
    suspend fun getPupil(id: Int): LocalPupil

    @Query("DELETE FROM Pupils")
    suspend fun deleteAllPupils()

    @Query("DELETE FROM pupils WHERE sync_status = 'SYNCED'")
    suspend fun deletePupilsWithSyncedStatus()

    @Query("DELETE FROM Pupils WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM Pupils WHERE sync_status != 'SYNCED'")
    suspend fun getUnsyncedPupils(): List<LocalPupil>

    @Query("SELECT * FROM Pupils WHERE sync_status = :status")
    suspend fun getPupilsBySyncStatus(status: SyncStatus): List<LocalPupil>


}