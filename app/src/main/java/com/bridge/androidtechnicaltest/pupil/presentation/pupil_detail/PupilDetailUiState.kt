package com.bridge.androidtechnicaltest.pupil.presentation.pupil_detail

import com.bridge.androidtechnicaltest.pupil.domain.model.PupilEntity

data class PupilDetailUiState(
    val loadState: PupilDetailLoadState = PupilDetailLoadState.Idle,
)

sealed class PupilDetailOutput {
    data class OnLoadError(val message: String): PupilDetailOutput()
}

sealed class PupilDetailLoadState {
    object Idle: PupilDetailLoadState()
    object Loading: PupilDetailLoadState()
    class Success(val pupilEntity: PupilEntity): PupilDetailLoadState()
}