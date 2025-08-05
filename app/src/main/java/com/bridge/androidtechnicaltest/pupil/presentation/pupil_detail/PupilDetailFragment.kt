package com.bridge.androidtechnicaltest.pupil.presentation.pupil_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bridge.androidtechnicaltest.R
import com.bridge.androidtechnicaltest.core.BaseFragment
import com.bridge.androidtechnicaltest.core.utils.launchScope
import com.bridge.androidtechnicaltest.core.utils.loadImage
import com.bridge.androidtechnicaltest.core.utils.orEmpty
import com.bridge.androidtechnicaltest.core.utils.showDoubleDialogAlert
import com.bridge.androidtechnicaltest.core.utils.showSingleDialogAlert
import com.bridge.androidtechnicaltest.core.utils.showSuccessToastMessage
import com.bridge.androidtechnicaltest.core.utils.visible
import com.bridge.androidtechnicaltest.databinding.FragmentPupildetailBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PupilDetailFragment : BaseFragment<FragmentPupildetailBinding>() {
    private val viewModel: PupilDetailViewModel by viewModel()
    private val args: PupilDetailFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return initBinding(
            FragmentPupildetailBinding.inflate(inflater, container, false),
            this
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeUi()
    }

    private fun observeUi() {
        launchScope {
            launch {
                viewModel.uiState.collectLatest { uiState ->
                    when (uiState.loadState) {
                        PupilDetailLoadState.Idle -> handleIdleState()
                        PupilDetailLoadState.Loading -> handleLoadingState()
                        PupilDetailLoadState.Success -> handleSuccessState()
                    }
                }
            }

            launch {
                viewModel.output.collectLatest { output ->
                    when (output) {
                        PupilDetailOutput.OnDeleteError -> {
                            showSingleDialogAlert(
                                title = getString(R.string.error_title_label),
                                content = getString(R.string.delete_pupil_error),
                            )
                        }

                        PupilDetailOutput.OnDeleteSuccess -> {
                            showSuccessToastMessage(getString(R.string.delete_pupil_success))
                            pop()
                        }

                        is PupilDetailOutput.OnLoadError -> {
                            showSingleDialogAlert(
                                title = getString(R.string.error_title_label),
                                content = output.message,
                                onCancel = {
                                    pop()
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun handleIdleState() {
        requireBinding().apply {
            loaderLayout.visible(false)
            contentLayout.visible(true)
        }
    }

    private fun handleSuccessState() {
        requireBinding().apply {
            loaderLayout.visible(false)
            contentLayout.visible(true)
            val uiState = viewModel.uiState.value
            pupilNameValueTv.text = uiState.pupilEntity?.name.orEmpty
            countryValueTv.text = uiState.pupilEntity?.country.orEmpty
            pupilImage.loadImage(uiState.pupilEntity?.image.orEmpty, errorImageRes = R.drawable.ic_user)
        }
    }

    private fun handleLoadingState() {
        requireBinding().apply {
            loaderLayout.visible(true)
            contentLayout.visible(false)
        }
    }

    private fun initView() {
        requireBinding().apply {
            viewModel.loadPupilDetails(args.pupilId)
            btnBack.setOnClickListener {
                pop()
            }

            editPupilDetailsBtn.setOnClickListener {
                val action = PupilDetailFragmentDirections.actionPupilDetailFragmentToAddEditPupilDetailFragment(args.pupilId)
                navigate(action)
            }

            deletePupilBtn.setOnClickListener{
                displayConfirmPupilDeletionDialog()
            }
        }
    }

    private fun displayConfirmPupilDeletionDialog() {
        showDoubleDialogAlert(
            title = getString(R.string.confirm_delete_pupil_dialog_title),
            content = getString(R.string.confirm_delete_pupil_dialog_content),
            primaryButtonText = getString(R.string.continue_label),
            secondaryButtonText = getString(R.string.cancel_label),
            onPrimaryButtonClicked = { dialog ->
                viewModel.deletePupil()
            }
        )
    }
}