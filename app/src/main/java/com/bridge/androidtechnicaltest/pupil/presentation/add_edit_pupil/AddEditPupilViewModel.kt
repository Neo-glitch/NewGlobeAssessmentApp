package com.bridge.androidtechnicaltest.pupil.presentation.add_edit_pupil

import androidx.lifecycle.ViewModel
import com.bridge.androidtechnicaltest.pupil.domain.usecases.AddUpdatePupilUseCase
import com.bridge.androidtechnicaltest.pupil.domain.usecases.DeletePupilUseCase

class AddEditPupilViewModel(
    private val addEditPupilUseCase: AddUpdatePupilUseCase,
    private val deletePupilUseCase: DeletePupilUseCase
): ViewModel() {

}