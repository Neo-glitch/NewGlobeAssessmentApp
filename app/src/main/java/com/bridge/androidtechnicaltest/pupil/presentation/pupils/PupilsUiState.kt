package com.bridge.androidtechnicaltest.pupil.presentation.pupils

import androidx.paging.PagingData
import com.bridge.androidtechnicaltest.pupil.domain.model.PupilEntity

data class PupilListUiState(
    val data: PagingData<PupilEntity> = PagingData.empty(),
    val loadState: PupilsLoadState = PupilsLoadState.Idle
)

sealed class PupilsLoadState {
    object Idle: PupilsLoadState()
    object Empty: PupilsLoadState()
    object Success: PupilsLoadState()
    data class Error(val message: String): PupilsLoadState()
    object InitialLoading: PupilsLoadState()
    object Refreshing: PupilsLoadState()
}