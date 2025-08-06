package com.bridge.androidtechnicaltest.pupil.presentation.pupils

data class PupilListUiState(
    val loadState: PupilsLoadState = PupilsLoadState.Idle
)

sealed class PupilsLoadState {
    object Idle: PupilsLoadState()
    object Empty: PupilsLoadState()
    object Success: PupilsLoadState()
    object Error: PupilsLoadState()
    data class InitialError(val message: String): PupilsLoadState()
    object InitialLoading: PupilsLoadState()
    object Refreshing: PupilsLoadState()
}