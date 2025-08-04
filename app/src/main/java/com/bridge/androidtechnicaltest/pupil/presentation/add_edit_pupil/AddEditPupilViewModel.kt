package com.bridge.androidtechnicaltest.pupil.presentation.add_edit_pupil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bridge.androidtechnicaltest.core.network.Resource
import com.bridge.androidtechnicaltest.core.utils.LocationHelper
import com.bridge.androidtechnicaltest.core.utils.generateRandomInt
import com.bridge.androidtechnicaltest.pupil.data.datasources.local.model.SyncStatus
import com.bridge.androidtechnicaltest.pupil.domain.model.PupilEntity
import com.bridge.androidtechnicaltest.pupil.domain.usecases.AddUpdatePupilUseCase
import com.bridge.androidtechnicaltest.pupil.domain.usecases.DeletePupilUseCase
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
            if (result is Resource.Success) {
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

    fun updateCountry(country: String) {
        _uiState.value = _uiState.value.copy(
            country = country
        )
    }

    fun updatePupilName(name: String) {
        _uiState.value = _uiState.value.copy(
            name = name
        )
    }

    fun updateImage(image: String) {
        _uiState.value = _uiState.value.copy(
            image = image
        )
    }

    fun savePupil() {
        viewModelScope.launch {

            _uiState.update { it.copy(loadState = AddEditLoadState.Loading) }

            val location = locationHelper.getCurrentLocation()

            if (location == null) {
                _uiState.update { it.copy(loadState = AddEditLoadState.Idle) }
                _output.emit(AddEditPupilOutput.OnLocationError)
                return@launch
            }

            val isAddOperation = pupil == null || pupil?.syncStatus == SyncStatus.PENDING_CREATE
            val updatedPupil: PupilEntity

            if (isAddOperation) {
                updatedPupil = PupilEntity(
                    name = _uiState.value.name,
                    country = _uiState.value.country,
                    image = _uiState.value.image,
                    latitude = location.first,
                    longitude = location.second,
                    syncStatus = SyncStatus.PENDING_CREATE
                )

                addEditPupilUseCase(updatedPupil, isPupilCreation = true)
                _uiState.update { it.copy(loadState = AddEditLoadState.Idle) }
                _output.emit(AddEditPupilOutput.OnEditSuccess)

            } else {
                updatedPupil = PupilEntity(
                    pupilId = pupil?.pupilId ?: generateRandomInt(),
                    name = _uiState.value.name,
                    country = _uiState.value.country,
                    image = _uiState.value.image,
                    latitude = location.first,
                    longitude = location.second,
                    syncStatus = SyncStatus.PENDING_UPDATE
                )

                addEditPupilUseCase(updatedPupil, isPupilCreation = false)
                _uiState.update { it.copy(loadState = AddEditLoadState.Idle) }
                _output.emit(AddEditPupilOutput.OnEditSuccess)
            }
        }
    }

//    fun getLatitudeAndLongitude(): Pair<Double, Double> {
//
//    }
}