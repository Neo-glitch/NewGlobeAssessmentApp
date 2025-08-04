package com.bridge.androidtechnicaltest.core.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import androidx.core.graphics.drawable.toDrawable
import androidx.databinding.DataBindingUtil
import com.bridge.androidtechnicaltest.R
import com.bridge.androidtechnicaltest.databinding.LayoutDialogAlertBinding

class DialogAlert {

    fun prompt(
        context: Context,
        title: String,
        content: String,
        buttonText: String,
        cancellable: Boolean = true,
        onCancel: () -> Unit = {},
        onButtonClicked: (Dialog) -> Unit = {}
    ) : Dialog {
        val binding = DataBindingUtil.inflate<LayoutDialogAlertBinding>(
            LayoutInflater.from(context),
            R.layout.layout_dialog_alert,
            null,
            false
        )
        val dialog = Dialog(context)

        dialog.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        dialog.setContentView(binding.root)
        binding.title.text = title
        binding.subTitle.text = content

        binding.actionBtn.apply {
            text = buttonText
            setOnClickListener { onButtonClicked.invoke(dialog); dialog.dismiss() }
        }
        dialog.setCancelable(cancellable)
        dialog.setOnCancelListener {
            onCancel()
        }
        return dialog
    }
}