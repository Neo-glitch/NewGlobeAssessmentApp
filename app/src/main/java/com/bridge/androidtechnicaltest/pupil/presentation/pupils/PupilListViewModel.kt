package com.bridge.androidtechnicaltest.pupil.presentation.pupils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.cachedIn
import com.bridge.androidtechnicaltest.pupil.domain.usecases.DeletePupilUseCase
import com.bridge.androidtechnicaltest.pupil.domain.usecases.GetPupilsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PupilListViewModel(
    private val getPupilsUseCase: GetPupilsUseCase,
    private val deletePupilUseCase: DeletePupilUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<PupilListUiState>(PupilListUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadPupils()
    }

    private fun loadPupils() {
        viewModelScope.launch {
            getPupilsUseCase()
                .cachedIn(viewModelScope)
                .collectLatest { data ->
                    _uiState.update {
                        it.copy(data = data)
                    }
                }
        }
    }

    private fun onLoadStateChanged(loadState: CombinedLoadStates) {

    }
}