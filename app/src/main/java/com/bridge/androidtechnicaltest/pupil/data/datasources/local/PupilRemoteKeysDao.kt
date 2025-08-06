package com.bridge.androidtechnicaltest.pupil.data.datasources.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.PupilRemoteKey

@Dao
interface PupilRemoteKeysDao {
    @Query("SELECT * FROM pupil_remote_keys WHERE pupilId = :id")
    suspend fun getRemoteKeyByPupilId(id: Int): PupilRemoteKey?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKeys: List<PupilRemoteKey>)

    @Query("DELETE FROM pupil_remote_keys")
    suspend fun clearRemoteKeys()

    @Query("DELETE FROM pupil_remote_keys WHERE pupilId = :id")
    suspend fun deleteById(id: Int)
}