package com.bridge.androidtechnicaltest.pupil.data.mapper

import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.ClearedUnSyncedPupil
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.LocalPupil
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.SyncStatus
import com.bridge.androidtechnicaltest.pupil.data.datasources.remote.model.RemotePupil
import com.bridge.androidtechnicaltest.pupil.domain.model.PupilEntity

fun LocalPupil.toEntity() : PupilEntity {
    return PupilEntity(
        id = id,
        pupilId = pupilId,
        name = name,
        country = country,
        image = image,
        latitude = latitude,
        longitude = longitude,
        syncStatus = syncStatus
    )
}

fun LocalPupil.toClearedUnsyncedPupil() : ClearedUnSyncedPupil {
    return ClearedUnSyncedPupil(
        id = id,
        pupilId = pupilId,
        name = name,
        country = country,
        image = image,
        latitude = latitude,
        longitude = longitude,
        lastModified = System.currentTimeMillis(),
        syncStatus = syncStatus
    )
}

fun PupilEntity.toModel(): LocalPupil {
    return LocalPupil(
        id = id,
        pupilId = pupilId,
        name = name,
        country = country,
        image = image,
        latitude = latitude,
        longitude = longitude,
        syncStatus = syncStatus
    )
}

fun RemotePupil.toLocal() : LocalPupil {
    return LocalPupil(
        pupilId = pupilId,
        name = name,
        country = country,
        image = image,
        latitude = latitude,
        longitude = longitude,
        syncStatus = SyncStatus.SYNCED
    )
}

