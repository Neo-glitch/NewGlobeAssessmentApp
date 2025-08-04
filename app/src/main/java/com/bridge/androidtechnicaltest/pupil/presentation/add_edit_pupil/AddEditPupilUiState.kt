package com.bridge.androidtechnicaltest.pupil.presentation.add_edit_pupil

import com.bridge.androidtechnicaltest.pupil.domain.model.PupilEntity

data class AddEditPupilUiState(
    val loadState: AddEditLoadState = AddEditLoadState.Idle,
    val name: String = "",
    val country: String = "",
    val image: String = ""
)

sealed class AddEditPupilOutput {
    data object onEditSuccess : AddEditPupilOutput()
    data object onDeleteSuccess : AddEditPupilOutput()
    data object onAddSuccess : AddEditPupilOutput()
    data object onEditError : AddEditPupilOutput()
    data object onDeleteError : AddEditPupilOutput()
    data object onAddError : AddEditPupilOutput()
}

sealed class AddEditLoadState {
    object Idle: AddEditLoadState()
    object Success: AddEditLoadState()
    object Error: AddEditLoadState()
    object Loading: AddEditLoadState()
}