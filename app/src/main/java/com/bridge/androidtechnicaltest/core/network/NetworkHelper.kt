package com.bridge.androidtechnicaltest.core.network

import com.bridge.androidtechnicaltest.core.ErrorResponse
import com.bridge.androidtechnicaltest.core.domain.Resource
import com.bridge.androidtechnicaltest.core.utils.GeneralExceptionHandler
import com.bridge.androidtechnicaltest.core.utils.K.CLIENT_ERROR_MSG
import com.bridge.androidtechnicaltest.core.utils.K.NETWORK_ERROR_MSG
import com.bridge.androidtechnicaltest.core.utils.K.NO_INTERNET_MSG
import com.bridge.androidtechnicaltest.core.utils.K.SERVER_ERROR_MSG
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Single
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import java.io.InterruptedIOException
import java.net.SocketException
import java.net.UnknownHostException


object NetworkHelper {

    suspend fun <T> handleApiCall(apiCall: suspend () -> T): Resource<T> {
        return try {
            val response = apiCall()
            Resource.Success(response)
        } catch (throwable: Throwable) {
            Resource.Error(GeneralExceptionHandler.getErrorMessage(throwable))
        }
    }

}