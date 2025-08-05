package com.bridge.androidtechnicaltest.pupil.presentation.pupil_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bridge.androidtechnicaltest.core.network.Resource
import com.bridge.androidtechnicaltest.pupil.domain.usecases.DeletePupilUseCase
import com.bridge.androidtechnicaltest.pupil.domain.usecases.GetPupilUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PupilDetailViewModel(
    private val getPupilUseCase: GetPupilUseCase,
    private val deletePupilUseCase: DeletePupilUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(PupilDetailUiState())
    val uiState = _uiState.asStateFlow()

    private val _output = MutableSharedFlow<PupilDetailOutput>()
    val output = _output.asSharedFlow()

    fun loadPupilDetails(id: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(loadState = PupilDetailLoadState.Loading) }

            val result = getPupilUseCase(id)
            when (result) {
                is Resource.Error -> {
                    _uiState.update { it.copy(loadState = PupilDetailLoadState.Idle) }
                    _output.emit(PupilDetailOutput.OnLoadError(result.message))
                }
                is Resource.Success -> {
                    _uiState.update { it.copy(loadState = PupilDetailLoadState.Success, pupilEntity = result.data) }
                }
            }
        }
    }

    fun deletePupil() {
        viewModelScope.launch {
            val pupil = _uiState.value.pupilEntity ?: return@launch

            _uiState.update { it.copy(loadState = PupilDetailLoadState.Loading) }

            when (val response = deletePupilUseCase(pupil)) {
                is Resource.Error -> {
                    _uiState.update { it.copy(loadState = PupilDetailLoadState.Idle) }
                    _output.emit(PupilDetailOutput.OnDeleteError)
                }
                is Resource.Success -> {
                    _uiState.update { it.copy(loadState = PupilDetailLoadState.Idle) }
                    _output.emit(PupilDetailOutput.OnDeleteSuccess)
                }
            }
        }
    }
}