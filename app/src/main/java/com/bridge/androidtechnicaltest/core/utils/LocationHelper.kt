package com.bridge.androidtechnicaltest.core.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

interface LocationHelper {
    suspend fun getCurrentLocation(): Pair<Double, Double>?
}

class LocationHelperImpl(private val context: Context, private val internetConnectivityManager: InternetConnectivityManager) : LocationHelper  {

    override suspend fun getCurrentLocation(): Pair<Double, Double>? {
        val fusedClient = LocationServices.getFusedLocationProviderClient(context)

        if (!internetConnectivityManager.isInternetAvailable()) {
            val lastLocation = fusedClient.lastLocation.await()
            return lastLocation?.let {
                Pair(it.latitude, it.longitude)
            }
        }

        return suspendCancellableCoroutine { continuation ->

            val request = LocationRequest.Builder(
                Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                0
            ).setMaxUpdates(1).build()

            val callback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    fusedClient.removeLocationUpdates(this)
                    result.lastLocation?.let {
                        continuation.resume(Pair(it.latitude, it.longitude))
                    } ?: continuation.resume(null)
                }
            }

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                fusedClient.requestLocationUpdates(request, callback, Looper.getMainLooper())

                continuation.invokeOnCancellation {
                    fusedClient.removeLocationUpdates(callback)
                }
            } else {
                continuation.resume(null)
            }
        }
    }

}