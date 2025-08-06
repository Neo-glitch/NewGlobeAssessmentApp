package com.bridge.androidtechnicaltest.pupil.presentation.add_edit_pupil

import com.bridge.androidtechnicaltest.pupil.domain.model.PupilEntity

data class AddEditPupilUiState(
    val loadState: AddEditLoadState = AddEditLoadState.Idle,
    val name: String = "",
    val country: String = "",
    val image: String = ""
)

sealed class AddEditPupilOutput {
    data object OnLocationError : AddEditPupilOutput()
    data object OnEditSuccess : AddEditPupilOutput()

    data class OnEditError(val message: String): AddEditPupilOutput()

    data class OnPupilLoadError(val message: String) : AddEditPupilOutput()
}

sealed class AddEditLoadState {
    object Idle: AddEditLoadState()
    object Loaded: AddEditLoadState()
    object Loading: AddEditLoadState()
}