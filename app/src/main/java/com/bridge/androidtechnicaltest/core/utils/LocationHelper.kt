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
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

interface ILocationHelper {
    suspend fun getCurrentLocation(): Pair<Double, Double>?
}

class LocationHelper(private val context: Context) : ILocationHelper  {

    override suspend fun getCurrentLocation(): Pair<Double, Double>? = suspendCoroutine { continuation ->
        val fusedClient = LocationServices.getFusedLocationProviderClient(context)

        val request = LocationRequest.create().apply {
            priority = Priority.PRIORITY_BALANCED_POWER_ACCURACY
            interval = 0
            numUpdates = 1
        }

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
        } else {
            continuation.resume(null)
        }
    }
}