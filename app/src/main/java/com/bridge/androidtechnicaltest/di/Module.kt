package com.bridge.androidtechnicaltest.di

import com.bridge.androidtechnicaltest.BuildConfig
import com.bridge.androidtechnicaltest.core.database.DatabaseFactory
import com.bridge.androidtechnicaltest.core.network.AuthInterceptor
import com.bridge.androidtechnicaltest.core.network.NetworkConnectionInterceptor
import com.bridge.androidtechnicaltest.core.network.HeaderInterceptor
import com.bridge.androidtechnicaltest.core.utils.ILocationHelper
import com.bridge.androidtechnicaltest.core.utils.K
import com.bridge.androidtechnicaltest.core.utils.K.API_TIMEOUT
import com.bridge.androidtechnicaltest.core.utils.LocationHelper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val databaseModule = module {
    single { DatabaseFactory.getDBInstance(get()) }
}

val locationModule = module {
    factory { LocationHelper(androidContext()) }.bind<ILocationHelper>()
}

val networkModule = module {

    single<OkHttpClient> {
        OkHttpClient.Builder()
            .readTimeout(API_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(API_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(API_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(NetworkConnectionInterceptor())
            .addInterceptor(AuthInterceptor())
            .addInterceptor(HeaderInterceptor())
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            })
            .build()
    }

    single {
        GsonConverterFactory.create()
    }


    single {
        Retrofit.Builder()
            .baseUrl(K.BASE_URL)
            .client(get<OkHttpClient>())
            .addConverterFactory(get<GsonConverterFactory>())
            .build()
    }
}
