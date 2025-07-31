package com.bridge.androidtechnicaltest.core.utils

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bridge.androidtechnicaltest.R
import io.github.muddz.styleabletoast.StyleableToast

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