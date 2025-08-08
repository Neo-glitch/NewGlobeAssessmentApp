package com.bridge.androidtechnicaltest.pupil.presentation.add_edit_pupil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bridge.androidtechnicaltest.core.domain.Resource
import com.bridge.androidtechnicaltest.core.utils.K
import com.bridge.androidtechnicaltest.core.utils.LocationHelper
import com.bridge.androidtechnicaltest.core.utils.orZero
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.SyncStatus
import com.bridge.androidtechnicaltest.pupil.domain.model.PupilEntity
import com.bridge.androidtechnicaltest.pupil.domain.usecases.AddUpdatePupilUseCase
import com.bridge.androidtechnicaltest.pupil.domain.usecases.GetPupilUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddEditPupilViewModel(
    private val locationHelper: LocationHelper,
    private val addEditPupilUseCase: AddUpdatePupilUseCase,
    private val getPupilUseCase: GetPupilUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(AddEditPupilUiState())
    val uiState = _uiState.asStateFlow()

    private val _output = MutableSharedFlow<AddEditPupilOutput>()
    val output = _output.asSharedFlow()

    private var pupil: PupilEntity? = null

    fun loadPupilDetails(id: Int) {
        if (pupil != null) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loadState = AddEditLoadState.Loading)

            val result = getPupilUseCase(id)
            when (result) {
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(loadState = AddEditLoadState.Idle)
                    _output.emit(AddEditPupilOutput.OnPupilLoadError(result.message))
                }
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        loadState = AddEditLoadState.Loaded,
                        name = result.data.name,
                        country = result.data.country,
                        image = result.data.image
                    )

                    pupil = result.data
                }
            }
        }
    }

    fun updateImage(image: String) {
        _uiState.value = _uiState.value.copy(
            image = image
        )
    }

    fun cacheData(country: String, name: String) {
        _uiState.value = _uiState.value.copy(
            country = country,
            name = name
        )
    }

    fun savePupil(
        name: String,
        country: String
    ) {
        cacheData(country, name)

        viewModelScope.launch {

            _uiState.update { it.copy(loadState = AddEditLoadState.Loading) }

            val location = locationHelper.getCurrentLocation()

            if (location == null) {
                _uiState.update { it.copy(loadState = AddEditLoadState.Idle) }
                _output.emit(AddEditPupilOutput.OnLocationError)
                return@launch
            }

            val isCreationOperation = pupil == null || pupil?.pupilId == K.TEMP_PUPIL_PUBLIC_ID
            val updatedPupil: PupilEntity

            if (isCreationOperation) {
                updatedPupil = PupilEntity(
                    name = _uiState.value.name,
                    country = _uiState.value.country,
                    image = _uiState.value.image,
                    latitude = location.first,
                    longitude = location.second,
                    syncStatus = SyncStatus.PENDING_CREATE
                )
            } else {
                updatedPupil = PupilEntity(
                    id = pupil?.id.orZero,
                    pupilId = pupil?.pupilId ?: -100,
                    name = _uiState.value.name,
                    country = _uiState.value.country,
                    image = _uiState.value.image,
                    latitude = location.first,
                    longitude = location.second,
                    syncStatus = SyncStatus.PENDING_UPDATE
                )
            }

            when (val response = addEditPupilUseCase(updatedPupil)) {
                is Resource.Error -> {
                    _uiState.update { it.copy(loadState = AddEditLoadState.Idle) }
                    _output.emit(AddEditPupilOutput.OnEditError(response.message))
                }
                is Resource.Success -> {
                    _uiState.update { it.copy(loadState = AddEditLoadState.Idle) }
                    _output.emit(AddEditPupilOutput.OnEditSuccess)
                }
            }
        }
    }
}