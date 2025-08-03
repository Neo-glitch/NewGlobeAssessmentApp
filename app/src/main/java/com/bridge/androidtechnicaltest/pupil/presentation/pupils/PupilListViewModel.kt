package com.bridge.androidtechnicaltest.pupil.presentation.pupils

import androidx.lifecycle.ViewModel
import com.bridge.androidtechnicaltest.pupil.domain.usecases.GetPupilsUseCase

class PupilListViewModel(
    private val getPupilsUseCase: GetPupilsUseCase,
) : ViewModel() {


}