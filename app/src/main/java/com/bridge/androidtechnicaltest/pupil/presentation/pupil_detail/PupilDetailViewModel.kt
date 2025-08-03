package com.bridge.androidtechnicaltest.pupil.presentation.pupil_detail

import androidx.lifecycle.ViewModel
import com.bridge.androidtechnicaltest.pupil.domain.usecases.GetPupilUseCase

class PupilDetailViewModel(
    private val getPupilUseCase: GetPupilUseCase
): ViewModel() {
}