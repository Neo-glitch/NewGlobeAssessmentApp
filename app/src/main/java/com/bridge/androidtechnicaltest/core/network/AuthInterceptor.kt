package com.bridge.androidtechnicaltest.core.network

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class AuthInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        if (response.code == 401) {
            throw InvalidTokenException()
        }

        return response
    }
}

class InvalidTokenException : IOException()