package com.bridge.androidtechnicaltest.core

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

interface BaseViewBinder<VB: ViewBinding> {

    val binding: VB?

    fun initBinding(binding: VB, fragment: Fragment): View

    fun requireBinding(): VB
}