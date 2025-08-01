package com.bridge.androidtechnicaltest.core.network

import com.bridge.androidtechnicaltest.core.ErrorResponse
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

//    fun <T> handleApiCallRx(apiCall: () -> Single<T>): Single<Resource<T>> {
//        return apiCall()
//            .map<Resource<T>> { response ->
//                Resource.Success(response)
////                if (response.isSuccessful) {
////                    Res
////                    val body = response.body()
////                    body?.let { Resource.Success(response) } ?: Resource.Error(SERVER_ERROR_MSG)
////                } else {
////                    val errorMessage = getErrorMessage(response)
////                    Resource.Error(errorMessage)
////                }
//            }
//            .onErrorReturn { throwable ->
//                Resource.Error(getErrorMessage(throwable))
//            }
//    }

    suspend fun <T> handleApiCall(apiCall: suspend () -> T): Resource<T> {
        return try {
            val response = apiCall()
            Resource.Success(response)
        } catch (throwable: Throwable) {
            Resource.Error(getErrorMessage(throwable))
        }
    }

    fun getErrorMessage(throwable: Throwable): String {
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