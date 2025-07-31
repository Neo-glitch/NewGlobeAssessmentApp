package com.bridge.androidtechnicaltest.di

import com.bridge.androidtechnicaltest.core.database.DatabaseFactory
import com.bridge.androidtechnicaltest.core.network.AuthInterceptor
import com.bridge.androidtechnicaltest.core.network.NetworkConnectionInterceptor
import com.bridge.androidtechnicaltest.core.network.RequestInterceptor
import com.bridge.androidtechnicaltest.core.utils.K
import com.bridge.androidtechnicaltest.core.utils.K.API_TIMEOUT
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val databaseModule = module {
    single { DatabaseFactory.getDBInstance(get()) }
}

val networkModule = module {

    single<OkHttpClient> {
        OkHttpClient.Builder()
            .readTimeout(API_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(API_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(API_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(NetworkConnectionInterceptor())
            .addInterceptor(AuthInterceptor())
            .addInterceptor(RequestInterceptor())
            .build()
    }

    // Provide Gson converter
    single {
        GsonConverterFactory.create()
    }

    // Provide RxJava2 CallAdapter
    single {
        RxJava2CallAdapterFactory.create()
    }

    // Provide Retrofit
    single {
        Retrofit.Builder()
            .baseUrl(K.BASE_URL) // Replace with your actual base URL
            .client(get<OkHttpClient>())
            .addCallAdapterFactory(get<RxJava2CallAdapterFactory>())
            .addConverterFactory(get<GsonConverterFactory>())
            .build()
    }
}
