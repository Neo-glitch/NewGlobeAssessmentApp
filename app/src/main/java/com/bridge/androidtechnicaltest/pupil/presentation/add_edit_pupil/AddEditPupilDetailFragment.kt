package com.bridge.androidtechnicaltest.pupil.presentation.add_edit_pupil

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.navArgs
import com.bridge.androidtechnicaltest.core.BaseFragment
import com.bridge.androidtechnicaltest.core.presentation.bottomsheet.ImageSourceBottomSheet
import com.bridge.androidtechnicaltest.core.presentation.model.ImageSourceType
import com.bridge.androidtechnicaltest.core.utils.createImageFile
import com.bridge.androidtechnicaltest.core.utils.enable
import com.bridge.androidtechnicaltest.core.utils.getCountryCodeByName
import com.bridge.androidtechnicaltest.core.utils.launchScope
import com.bridge.androidtechnicaltest.core.utils.loadImage
import com.bridge.androidtechnicaltest.core.utils.saveImageToInternalStorage
import com.bridge.androidtechnicaltest.core.utils.showErrorToastMessage
import com.bridge.androidtechnicaltest.core.utils.showSuccessToastMessage
import com.bridge.androidtechnicaltest.core.utils.showWarningToastMessage
import com.bridge.androidtechnicaltest.databinding.FragmentAddEditPupilDetailBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddEditPupilDetailFragment : BaseFragment<FragmentAddEditPupilDetailBinding>() {

    private val addEditNavArgs by navArgs<AddEditPupilDetailFragmentArgs>()
    private val viewmodel: AddEditPupilViewModel by viewModel()

    private lateinit var photoUri: Uri

    private val locationPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        if (!granted) {
            showWarningToastMessage("Location permission denied")
        }
    }

    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
           if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
               requestStoragePermission()
           } else {
               openCamera()
           }
        } else {
            showWarningToastMessage("Camera permission denied")
        }
    }

    private val storagePermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            openCamera()
        } else {
            showWarningToastMessage("Storage permission denied")
        }
    }

    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            handleImagePicked(photoUri)
            updateImageView()
        }
    }

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { handleImagePicked(it) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return initBinding(
            FragmentAddEditPupilDetailBinding.inflate(inflater, container, false),
            this
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeView()
    }

    private fun initView() {
        requireBinding().apply {
            checkAndRequestLocationPermission()

            if (addEditNavArgs.pupilId != -1) {
                viewmodel.loadPupilDetails(addEditNavArgs.pupilId)
                title.text = "Edit Pupil"
            } else {
                title.text = "Add Pupil"
            }

            countryPicker.setOnCountryChangeListener {
                val countryName = countryPicker.selectedCountryName
                viewmodel.updateCountry(countryName)
            }

            pupilNameInput.addTextChangedListener { name ->
                viewmodel.updatePupilName(name.toString())
                updateSaveButton()
            }

            editImageContainer.setOnClickListener {
                openImageSourceSelectionBottomSheet()
            }

            saveBtn.setOnClickListener {
                savePupilDetail()
            }

            btnBack.setOnClickListener { pop() }

            updateCountryCodePickerView()
            updateImageView()
        }
    }

    private fun observeView() {
        launchScope {
            launch {
                viewmodel.uiState.collect { state ->
                    when (state.loadState) {
                        AddEditLoadState.Idle -> {
                            requireBinding().apply {
                                pupilImage.enable(true)
                                countryPicker.enable(true)
                                countryPickerFrame.enable(true)
                                editImageContainer.enable(true)
                                updateSaveButton()
                            }
                        }

                        AddEditLoadState.Loaded -> {
                            updateFields()
                        }

                        AddEditLoadState.Loading -> {
                            requireBinding().apply {
                                pupilImage.enable(false)
                                countryPicker.enable(false)
                                countryPickerFrame.enable(false)
                                editImageContainer.enable(false)
                                updateSaveButton()
                            }
                        }
                    }
                }
            }

            launch {
                viewmodel.output.collect { output ->
                    when (output) {
                        AddEditPupilOutput.OnEditSuccess -> {
                            showSuccessToastMessage("Pupil details Saved successfully")
                            pop()
                        }
                        AddEditPupilOutput.OnLocationError -> {
                            showErrorToastMessage("Can't get current location, Please ensure location services is turned on")
                        }
                    }
                }
            }
        }
    }

    private fun updateFields() {
        requireBinding().apply {
            val uiState = viewmodel.uiState.value
            pupilNameInput.setText(uiState.name)
            updateImageView()
            updateCountryCodePickerView()
        }
    }

    private fun updateImageView() {
        requireBinding().apply {
            val uiState = viewmodel.uiState.value
            uiState.takeIf { uiState.image.isNotBlank() }?.let {
                pupilImage.loadImage(uiState.image)
            }
            updateSaveButton()
        }
    }

    private fun updateSaveButton() {
        requireBinding().apply {
            val uiState = viewmodel.uiState.value
            val isLoading = uiState.loadState is AddEditLoadState.Loading

            val isBtnEnabled = uiState.name.isNotBlank() && uiState.country.isNotBlank() && uiState.image.isNotBlank()
            saveBtn.enable(isBtnEnabled)
            saveBtn.text = if (isLoading) "Saving..." else "Save"
        }
    }

    private fun updateCountryCodePickerView() {
        requireBinding().apply {
            val uiState = viewmodel.uiState.value
            getCountryCodeByName(uiState.country)?.let {
                countryPicker.setCountryForNameCode(it)
            }
            updateSaveButton()
        }
    }

    private fun savePupilDetail() {
        requireBinding().apply {
            if (isLocationPermissionGranted()) {
                viewmodel.savePupil()
            } else {
                showErrorToastMessage("Location permission is needed to save a pupil details")
            }
        }
    }

    private fun openImageSourceSelectionBottomSheet() {
        val selectImageBottomSheet = ImageSourceBottomSheet.newInstance(
            onImageSourceSelected = { source ->
                when (source) {
                    ImageSourceType.Gallery -> openGallery()
                    ImageSourceType.Camera -> requestCameraPermission()
                }
            }
        )
        selectImageBottomSheet.show(childFragmentManager, "IMAGE_CHOOSER")
    }

    private fun openCamera() {
        val file = createImageFile(requireContext())
        photoUri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.provider",
            file
        )
        cameraLauncher.launch(photoUri)
    }

    private fun openGallery() {
        pickImageLauncher.launch("image/*")
    }

    private fun handleImagePicked(imageUri: Uri) {
        val imagePath = saveImageToInternalStorage(imageUri, requireContext())
        viewmodel.updateImage(imagePath)
        updateImageView()
    }

    private fun requestCameraPermission() {
        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    private fun requestStoragePermission() {
        storagePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    private fun checkAndRequestLocationPermission() {
        if (!isLocationPermissionGranted()) {
            locationPermissionsLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
        }
    }

    private fun isLocationPermissionGranted(): Boolean {
        val fine = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
        val coarse = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
        return fine == PackageManager.PERMISSION_GRANTED || coarse == PackageManager.PERMISSION_GRANTED
    }

//    private fun isCameraPermissionGranted(): Boolean {
//        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
//    }
//
//    private fun isStoragePermissionGranted(): Boolean {
//        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
//    }
}