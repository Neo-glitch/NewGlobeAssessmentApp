package com.bridge.androidtechnicaltest.core

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.graphics.drawable.toDrawable
import androidx.core.net.toUri
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.bridge.androidtechnicaltest.R
import com.bridge.androidtechnicaltest.core.utils.printDebugStackTrace

abstract class BaseDialogFragment<VB : ViewBinding> : DialogFragment(),
    FragmentNavigationDispatcher, BaseViewBinder<VB> by BaseViewBindingImpl() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBehaviour(true)
    }

    protected open fun setBehaviour(isCancellable: Boolean = true, fillScreen: Boolean = false) {
        isCancelable = isCancellable
    }

    override fun navigate(deepLinks: String) {
        try {
            val request = NavDeepLinkRequest.Builder.fromUri(deepLinks.toUri())
                .build()

            val navOptions =
                NavOptions.Builder()
                    .build()

            findNavController().navigate(request, navOptions)
        } catch (ex: Exception) {
            ex.printDebugStackTrace()
        }
    }

    override fun navigate(destination: NavDirections, bundle: Bundle?, navOptions: NavOptions?) {
        try {
            findNavController().navigate(destination.actionId, bundle)
        } catch (ex: Exception) {
            ex.printDebugStackTrace()
        }
    }

    override fun navigate(destination: NavDirections) {
        try {
            findNavController().navigate(destination)
        } catch (ex: Exception) {
            ex.printDebugStackTrace()
        }
    }

    override fun <T> navigate(clazz: Class<T>) {
        Intent(requireContext(), clazz).apply {
            startActivity(this)
            requireActivity().finish()
        }
    }

    override fun navigate(intent: Intent) {
        startActivity(intent)
        requireActivity().finish()
    }

    override fun navigate(id: Int) {
        try {
            findNavController().navigate(id)
        } catch (ex: Exception) {
            ex.printDebugStackTrace()
        }
    }

    override fun pop(to: Int?, inclusive: Boolean?) {
        if (to != null) {
            findNavController().popBackStack(to, inclusive ?: false)
            return
        }

        findNavController().navigateUp()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Bridge_Dialog)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        return dialog
    }
}