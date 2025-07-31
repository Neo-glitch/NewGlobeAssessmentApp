package com.bridge.androidtechnicaltest.pupil.data.mapper

import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.LocalPupil
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.SyncStatus
import com.bridge.androidtechnicaltest.pupil.data.datasources.remote.model.RemotePupil
import com.bridge.androidtechnicaltest.pupil.domain.model.PupilEntity

fun LocalPupil.toEntity() : PupilEntity {
    return PupilEntity(
        pupilId = pupilId,
        name = name,
        country = country,
        image = image,
        latitude = latitude,
        longitude = longitude,
        syncStatus = syncStatus
    )
}

fun PupilEntity.toModel(): LocalPupil {
    return LocalPupil(
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

