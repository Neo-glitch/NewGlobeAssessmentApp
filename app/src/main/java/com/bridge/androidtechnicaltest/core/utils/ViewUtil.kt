package com.bridge.androidtechnicaltest.core.utils

import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bridge.androidtechnicaltest.R
import com.bumptech.glide.Glide
import io.github.muddz.styleabletoast.StyleableToast
import androidx.core.net.toUri
import java.io.File
import java.util.Locale

fun View.hide(invisible: Boolean = false) {
    visibility = if (invisible) View.INVISIBLE else View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.visible(show: Boolean) {
    if (show) show() else hide()
}

fun View.enable(enable: Boolean) {
    if (enable) {
        alpha = 1f
        isClickable = true
        isEnabled = true
    } else {
        alpha = .5f
        isClickable = false
        isEnabled = false
    }
}

fun Fragment.showSuccessToastMessage(message: String, duration: Int = Toast.LENGTH_SHORT) {
    StyleableToast.makeText(this.requireContext(), message, duration, R.style.SuccessToast)
        .show()
}

fun Fragment.showErrorToastMessage(message: String, duration: Int = Toast.LENGTH_SHORT) {
    StyleableToast.makeText(this.requireContext(), message, duration, R.style.ErrorToast)
        .show()
}

fun Fragment.showWarningToastMessage(message: String, duration: Int = Toast.LENGTH_SHORT) {
    StyleableToast.makeText(this.requireContext(), message, duration, R.style.WarningToast)
        .show()
}

fun Fragment.showSingleDialogAlert(
    title: String,
    content: String?,
    buttonText: String? = null,
    cancellable: Boolean = false,
    onCancel: () -> Unit = {},
    onButtonClicked: (Dialog) -> Unit = {}
) {
    if (content.isNullOrBlank()) return

    SingleActionDialogAlert().prompt(
        context = requireContext(),
        title = title,
        content = content,
        buttonText = buttonText ?: getString(R.string.continue_label),
        cancellable = cancellable,
        onCancel = onCancel,
        onButtonClicked = onButtonClicked
    ).show()
}

fun Fragment.showDoubleDialogAlert(
    title: String,
    content: String?,
    primaryButtonText: String? = null,
    secondaryButtonText: String? = null,
    cancellable: Boolean = false,
    onCancel: () -> Unit = {},
    onPrimaryButtonClicked: (Dialog) -> Unit = {},
    onSecondaryButtonClicked: (Dialog) -> Unit = {}
) {
    if (content.isNullOrBlank()) return
    DoubleActionDialogAlert().prompt(
        context = requireContext(),
        title = title,
        content = content,
        primaryButtonText = primaryButtonText ?: getString(R.string.continue_label),
        secondaryButtonText = secondaryButtonText ?: getString(R.string.cancel_label),
        cancellable = cancellable,
        onCancel = onCancel,
        onPrimaryButtonClicked = onPrimaryButtonClicked,
        onSecondaryButtonClicked = onSecondaryButtonClicked
    ).show()
}

fun ImageView.loadImage(
    image: String,
    errorImageRes: Int = R.drawable.ic_error_image,
    placeHolderImageRes: Int = R.drawable.ic_loading_image
) {
    if (image.isBlank()) {
        setImageResource(errorImageRes)
        return
    }

    val imageUri = if (image.isRemoteImage()) {
        image.toUri()
    } else {
        File(image).let { file ->
            if (file.exists()) Uri.fromFile(file) else null
        }
    }

    Glide.with(context)
        .load(imageUri)
        .placeholder(placeHolderImageRes)
        .error(errorImageRes)
        .into(this)
}

