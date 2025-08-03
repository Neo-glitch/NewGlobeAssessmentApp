package com.bridge.androidtechnicaltest.core.utils

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

fun View.hide(invisible: Boolean = false) {
    visibility = if (invisible) View.INVISIBLE else View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.visible(show: Boolean) {
    if (show) show() else hide()
}

fun Fragment.showToast(message: String, duration : Int = Toast.LENGTH_SHORT){
    StyleableToast.makeText(this.requireContext(), message, duration, R.style.ToastStyle).show()
}
//
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