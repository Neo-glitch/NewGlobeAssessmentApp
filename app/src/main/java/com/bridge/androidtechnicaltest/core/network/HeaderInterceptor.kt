package com.bridge.androidtechnicaltest.core.network

import com.bridge.androidtechnicaltest.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .addHeader("X-Request-ID", BuildConfig.REQUEST_ID)
            .addHeader("User-Agent", BuildConfig.USER_AGENT)
            .build()
        return chain.proceed(newRequest)
    }
}