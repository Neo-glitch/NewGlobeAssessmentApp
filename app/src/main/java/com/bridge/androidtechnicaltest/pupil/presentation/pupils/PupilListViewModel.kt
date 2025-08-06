package com.bridge.androidtechnicaltest.pupil.presentation.pupils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.cachedIn
import com.bridge.androidtechnicaltest.pupil.domain.usecases.GetPupilsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PupilListViewModel(
    private val getPupilsUseCase: GetPupilsUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(PupilListUiState())
    val uiState = _uiState.asStateFlow()

    val pupils = getPupilsUseCase().cachedIn(viewModelScope)


    fun onLoadStateChanged(
        loadStates: CombinedLoadStates,
        itemCount: Int,
    ) {
        val isListEmpty =
            loadStates.refresh is LoadState.NotLoading &&
                    loadStates.append.endOfPaginationReached &&
                    itemCount == 0
        val isInitialLoad = loadStates.refresh is LoadState.Loading && itemCount == 0
        val isInitialError = loadStates.refresh is LoadState.Error && itemCount == 0
        val isError = loadStates.refresh is LoadState.Error && itemCount > 0
        val hasLoaded = loadStates.refresh is LoadState.NotLoading && itemCount > 0
        val isRefreshing = loadStates.refresh is LoadState.Loading && itemCount > 0

        when {
            isInitialLoad -> {
                _uiState.update { it.copy(loadState = PupilsLoadState.InitialLoading) }
            }
            isRefreshing -> {
                _uiState.update { it.copy(loadState = PupilsLoadState.Refreshing) }
            }
            isListEmpty -> {
                _uiState.update { it.copy(loadState = PupilsLoadState.Empty) }
            }
            isInitialError -> {
                val error = (loadStates.refresh as LoadState.Error).error
                _uiState.update { it.copy(loadState = PupilsLoadState.InitialError(error.message ?: "Unknown error occurred")) }
            }
            hasLoaded -> {
                _uiState.update { it.copy(loadState = PupilsLoadState.Success) }
            }
            isError -> {
                _uiState.update { it.copy(loadState = PupilsLoadState.Error) }
            }
            else -> return
        }
    }
}