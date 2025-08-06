package com.bridge.androidtechnicaltest.core.utils

import com.bridge.androidtechnicaltest.BuildConfig

fun Exception.printDebugStackTrace() {
    if (BuildConfig.DEBUG) {
        printStackTrace()
    }
}