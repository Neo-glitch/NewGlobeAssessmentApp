package com.bridge.androidtechnicaltest.core.network

import com.bridge.androidtechnicaltest.core.utils.K.CLIENT_ERROR_MSG
import com.bridge.androidtechnicaltest.core.utils.K.NETWORK_ERROR_MSG
import com.bridge.androidtechnicaltest.core.utils.K.NO_INTERNET_MSG
import com.bridge.androidtechnicaltest.core.utils.K.SERVER_ERROR_MSG
import io.reactivex.Single
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.io.InterruptedIOException
import java.net.SocketException
import java.net.UnknownHostException


object NetworkHelper {



    fun <T> handleApiCallRx(apiCall: () -> Single<Response<T>>): Single<Resource<T>> {
        return apiCall()
            .map<Resource<T>> { response ->
                if (response.isSuccessful) {
                    val body = response.body()
                    body?.let { Resource.Success(body) } ?: Resource.Error(SERVER_ERROR_MSG)
                } else {
                    val errorMessage = getErrorMessage(response)
                    Resource.Error(errorMessage)
                }
            }
            .onErrorReturn { throwable ->
                Resource.Error(getErrorMessage(throwable))
            }
    }

    private inline fun <T> getErrorMessage(response: Response<T>): String {
        val errorBodyString = response.errorBody()?.string()!!
        var errorMsg: String
        var jsonObject: JSONObject? = null
        try {
            jsonObject = JSONObject(errorBodyString)
            errorMsg = jsonObject.getString("message")
        } catch (e: JSONException) {
            e.printStackTrace()
            errorMsg = "Wrong formatting"
        }
        return errorMsg
    }

    private inline fun getErrorMessage(throwable: Throwable): String {
        return when(throwable) {
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

}