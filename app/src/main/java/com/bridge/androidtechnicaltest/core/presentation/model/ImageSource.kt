package com.bridge.androidtechnicaltest.core.presentation.model

import androidx.annotation.DrawableRes

data class ImageSource(
    @DrawableRes val icon: Int,
    val description: String,
    val sourceType: ImageSourceType
)


enum class ImageSourceType {
    Gallery, Camera
}