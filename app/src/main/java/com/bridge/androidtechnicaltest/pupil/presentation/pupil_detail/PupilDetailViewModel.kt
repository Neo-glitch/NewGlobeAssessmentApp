package com.bridge.androidtechnicaltest.pupil.presentation.pupil_detail

import androidx.lifecycle.ViewModel
import com.bridge.androidtechnicaltest.pupil.domain.usecases.DeletePupilUseCase
import com.bridge.androidtechnicaltest.pupil.domain.usecases.GetPupilUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PupilDetailViewModel(
    private val getPupilUseCase: GetPupilUseCase,
    private val deletePupilUseCase: DeletePupilUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(PupilDetailUiState())
    val uiState = _uiState.asStateFlow()
}