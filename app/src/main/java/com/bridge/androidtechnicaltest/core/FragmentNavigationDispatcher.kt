package com.bridge.androidtechnicaltest.core

import android.content.Intent
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions

interface FragmentNavigationDispatcher {
    fun pop(to: Int? = null, inclusive: Boolean? = null)
    fun navigate(deepLinks: String)
    fun navigate(destination: NavDirections, bundle: Bundle? = null, navOptions: NavOptions? = null)
    fun navigate(destination: NavDirections)
    fun <T> navigate(clazz: Class<T>)
    fun navigate(intent: Intent)
    fun navigate(@IdRes id: Int)
}