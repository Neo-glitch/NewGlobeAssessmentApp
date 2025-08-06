package com.bridge.androidtechnicaltest.core

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.bridge.androidtechnicaltest.core.utils.KeyboardUtil
import com.bridge.androidtechnicaltest.core.utils.printDebugStackTrace
import com.bridge.androidtechnicaltest.core.utils.showErrorToastMessage
import com.bridge.androidtechnicaltest.core.utils.showSuccessToastMessage
import com.bridge.androidtechnicaltest.core.utils.showWarningToastMessage

abstract class BaseFragment<VB: ViewBinding> : Fragment(), FragmentNavigationDispatcher, BaseViewBinder<VB> by BaseViewBindingImpl() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun showSuccessToast(
        msg: String,
        duration: Int = Toast.LENGTH_LONG
    ) {
        showSuccessToastMessage(msg, duration)
    }

    fun showErrorToast(
        msg: String,
        duration: Int = Toast.LENGTH_LONG
    ) {
        showErrorToastMessage(msg, duration)
    }

    fun showWarningToast(
        msg: String,
        duration: Int = Toast.LENGTH_LONG
    ) {
        showWarningToastMessage(msg, duration)
    }

    override fun navigate(deepLinks: String) {
        try {
            val uri = deepLinks.toUri()
            val requestBuilder = NavDeepLinkRequest.Builder.fromUri(uri)
            val request = requestBuilder.build()

            val navOptions =
                NavOptions.Builder().build()

            findNavController().navigate(request, navOptions)
        } catch (ex: Exception) {
            ex.printDebugStackTrace()
        }
    }

    override fun navigate(destination: NavDirections, bundle: Bundle?, navOptions: NavOptions?) {
        try {
            findNavController().navigate(destination.actionId, bundle, navOptions)
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
}