package com.bridge.androidtechnicaltest.pupil.data.datasources.local;

import androidx.room.Dao;
import androidx.room.Query;

import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.LocalPupil;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface PupilDao {

    @Query("SELECT * FROM Pupils ORDER BY name ASC")
    Single<List<LocalPupil>> getPupils();
}
