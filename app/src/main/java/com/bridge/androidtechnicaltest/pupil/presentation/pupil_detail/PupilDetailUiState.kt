package com.bridge.androidtechnicaltest.pupil.presentation.pupil_detail

import com.bridge.androidtechnicaltest.pupil.domain.model.PupilEntity

data class PupilDetailUiState(
    val pupilEntity: PupilEntity? = null,
    val loadState: PupilDetailLoadState = PupilDetailLoadState.Idle,
)

sealed class PupilDetailOutput {
    data class OnLoadError(val message: String): PupilDetailOutput()
    data object OnDeleteSuccess : PupilDetailOutput()
    data object OnDeleteError : PupilDetailOutput()
}

sealed class PupilDetailLoadState {
    object Idle: PupilDetailLoadState()
    object Loading: PupilDetailLoadState()
    data object Success: PupilDetailLoadState()
}