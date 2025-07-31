package com.bridge.androidtechnicaltest.core.network

sealed class Resource<out T> {
    data class Success<out T>(val data: T): Resource<T>()
    data class Error(val message: String): Resource<Nothing>()
//    object Loading : Result<Nothing>()
}