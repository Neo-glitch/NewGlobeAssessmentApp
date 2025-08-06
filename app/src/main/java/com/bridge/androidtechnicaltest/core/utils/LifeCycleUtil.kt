package com.bridge.androidtechnicaltest.core.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

fun <T> LifecycleOwner.collectInLifecycleScope(
    flow: Flow<T>,
    lifecycleState : Lifecycle.State = Lifecycle.State.STARTED,
    action: suspend (T) -> Unit
) {
    this.lifecycleScope.launch {
        this@collectInLifecycleScope.lifecycle.repeatOnLifecycle(lifecycleState) {
            flow.collectLatest {
                action.invoke(it)
            }
        }
    }
}

fun Fragment.launchScope(state: Lifecycle.State = Lifecycle.State.STARTED,  block: suspend CoroutineScope.() -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(state) {
            block.invoke(this)
        }
    }
}