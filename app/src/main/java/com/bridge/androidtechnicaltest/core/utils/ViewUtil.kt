package com.bridge.androidtechnicaltest.core.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bridge.androidtechnicaltest.R
import com.bumptech.glide.Glide
import io.github.muddz.styleabletoast.StyleableToast
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import com.bridge.androidtechnicaltest.databinding.LayoutDialogAlertBinding
import java.io.File
import androidx.core.graphics.drawable.toDrawable

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
    StyleableToast.makeText(this.requireContext(), message, duration, R.style.ToastStyle_Success).show()
}

fun Fragment.showErrorToastMessage(message: String, duration: Int = Toast.LENGTH_SHORT) {
    StyleableToast.makeText(this.requireContext(), message, duration, R.style.ToastStyle_Error).show()
}

fun Fragment.showWarningToastMessage(message: String, duration: Int = Toast.LENGTH_SHORT) {
    StyleableToast.makeText(this.requireContext(), message, duration, R.style.ToastStyle_Warning).show()
}

fun Context.showDialogAlert(
    title: String,
    content: String?,
    buttonText: String? = null,
    cancellable: Boolean = false,
    onCancel: () -> Unit = {},
    onButtonClicked: (Dialog) -> Unit = {}
) {
    if (content.isNullOrBlank()) return

    DialogAlert().prompt(
        context = this,
        title = title,
        content = content,
        buttonText = buttonText ?: getString(R.string.continue_label),
        cancellable = cancellable,
        onCancel = onCancel,
        onButtonClicked = onButtonClicked
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

    val imageUri = if (image.startsWith("http")) {
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