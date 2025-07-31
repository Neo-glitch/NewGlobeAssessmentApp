package com.bridge.androidtechnicaltest.core

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding

class BaseViewBindingImpl<VB: ViewBinding> : BaseViewBinder<VB>, LifecycleEventObserver {

    override var binding: VB? = null

    private var lifecycle: Lifecycle? = null

    private lateinit var fragmentName: String

    private fun onDestroyView() {
        lifecycle?.removeObserver(this)
        lifecycle = null
        binding = null
    }

    override fun initBinding(
        binding: VB,
        fragment: Fragment
    ): View {
        this.binding = binding
        lifecycle = fragment.viewLifecycleOwner.lifecycle
        lifecycle?.addObserver(this)
        fragmentName = fragment::class.simpleName ?: "UnAvailable"
        return binding.root
    }

    override fun requireBinding(): VB {
        return binding ?: throw IllegalStateException("Binding is null and can't be accessed outside $fragmentName lifecycle")
    }

    override fun onStateChanged(
        source: LifecycleOwner,
        event: Lifecycle.Event
    ) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            onDestroyView()
        }
    }
}