package com.bridge.androidtechnicaltest.core.utils

import android.database.sqlite.SQLiteException
import com.bridge.androidtechnicaltest.core.ErrorResponse
import com.bridge.androidtechnicaltest.core.network.InvalidTokenException
import com.bridge.androidtechnicaltest.core.network.NoConnectivityException
import com.bridge.androidtechnicaltest.core.utils.K.CLIENT_ERROR_MSG
import com.bridge.androidtechnicaltest.core.utils.K.DATABASE_ERROR_MSG
import com.bridge.androidtechnicaltest.core.utils.K.NETWORK_ERROR_MSG
import com.bridge.androidtechnicaltest.core.utils.K.NO_INTERNET_MSG
import com.bridge.androidtechnicaltest.core.utils.K.SERVER_ERROR_MSG
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import java.io.InterruptedIOException
import java.net.SocketException
import java.net.UnknownHostException

object GeneralExceptionHandler {

    fun getErrorMessage(throwable: Throwable): String {
        throwable.printStackTrace()
        return when(throwable) {
            is CancellationException -> throw throwable
            is HttpException -> {
                when(val data = convertErrorBody<ErrorResponse>(throwable)) {
                    is ErrorResponse -> data.type
                    else -> SERVER_ERROR_MSG
                }
            }
            is NoConnectivityException -> NO_INTERNET_MSG
            is UnknownHostException -> throwable.message ?: SERVER_ERROR_MSG
            is InvalidTokenException -> CLIENT_ERROR_MSG
            is InterruptedIOException -> SERVER_ERROR_MSG
            is SocketException -> NETWORK_ERROR_MSG
            is SQLiteException, is IllegalStateException -> {
                if (throwable.message?.contains("Room", ignoreCase = true) == true ||
                    throwable.message?.contains("SQLite", ignoreCase = true) == true ||
                    throwable.message?.contains("database", ignoreCase = true) == true) {
                    DATABASE_ERROR_MSG
                } else {
                    CLIENT_ERROR_MSG
                }
            }
            else -> {
                throwable.printStackTrace()
                CLIENT_ERROR_MSG
            }
        }
    }

    inline fun <reified T> convertErrorBody(t: HttpException): T? {
        return try {
            t.response()?.errorBody()?.let {
                val type = object : TypeToken<T>() {}.type
                Gson().fromJson(it.charStream(), type)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}