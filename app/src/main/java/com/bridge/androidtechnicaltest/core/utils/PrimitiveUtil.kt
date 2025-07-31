package com.bridge.androidtechnicaltest.core.utils

import kotlin.random.Random

val Long?.orZero
    get() = this ?: 0L

val Double?.orZero
    get() = this ?: 0.0

val Int?.orZero
    get() = this ?: 0

val Float?.orZero
    get() = this ?: 0F

val Boolean?.orFalse
    get() = this ?: false

val String?.orEmpty
    get() = this ?: ""

fun generateRandomInt() : Int = Random.nextInt(1_000_000, Int.MAX_VALUE)