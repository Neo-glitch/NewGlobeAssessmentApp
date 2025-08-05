package com.bridge.androidtechnicaltest.core.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import androidx.core.graphics.drawable.toDrawable
import androidx.databinding.DataBindingUtil
import com.bridge.androidtechnicaltest.R
import com.bridge.androidtechnicaltest.databinding.LayoutDualActionDialogAlertBinding
import com.bridge.androidtechnicaltest.databinding.LayoutSingleActionDialogAlertBinding

class DoubleActionDialogAlert {

    fun prompt(
        context: Context,
        title: String,
        content: String,
        primaryButtonText: String,
        secondaryButtonText: String,
        cancellable: Boolean = true,
        onCancel: () -> Unit = {},
        onPrimaryButtonClicked: (Dialog) -> Unit = {},
        onSecondaryButtonClicked: (Dialog) -> Unit = {}
    ) : Dialog {
        val binding = DataBindingUtil.inflate<LayoutDualActionDialogAlertBinding>(
            LayoutInflater.from(context),
            R.layout.layout_dual_action_dialog_alert,
            null,
            false
        )
        val dialog = Dialog(context)

        dialog.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        dialog.setContentView(binding.root)
        binding.title.text = title
        binding.subTitle.text = content

        binding.primaryActionBtn.apply {
            text = primaryButtonText
            setOnClickListener { onPrimaryButtonClicked.invoke(dialog); dialog.dismiss() }
        }

        binding.secondaryActionBtn.apply {
            text = secondaryButtonText
            setOnClickListener { onSecondaryButtonClicked.invoke(dialog); dialog.dismiss() }
        }

        dialog.setCancelable(cancellable)
        dialog.setOnCancelListener {
            onCancel()
        }
        return dialog
    }
}