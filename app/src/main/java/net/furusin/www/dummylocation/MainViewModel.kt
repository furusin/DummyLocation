package net.furusin.www.dummylocation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.location.provider.ProviderProperties
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel : ViewModel() {
    val latitude = MutableStateFlow("")
    val longitude = MutableStateFlow("")

    fun updateLatitude(value: String) {
        latitude.value = value
    }

    fun updateLongitude(value: String) {
        longitude.value = value
    }

    fun setMockLocation(
        locationManager: LocationManager
    ): SetMockLocationResult {
        val providers = listOf(LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER)

        try {
            for (provider in providers) {
                Log.d("@@@", "$provider, longitude:$longitude, latitude:$latitude")
                locationManager.run {
                    addTestProvider(
                        provider,
                        false,
                        false,
                        false,
                        false,
                        false,
                        false,
                        false,
                        ProviderProperties.POWER_USAGE_LOW,
                        ProviderProperties.ACCURACY_FINE
                    )
                    setTestProviderEnabled(provider, true)

                    val mockLocation = Location(provider).also { location ->
                        location.latitude = latitude.value.toDouble()
                        location.longitude = longitude.value.toDouble()
                        location.altitude = 0.0
                        location.accuracy = 100f
                        location.time = System.currentTimeMillis()
                        location.speed = 0f
                        location.elapsedRealtimeNanos =
                            android.os.SystemClock.elapsedRealtimeNanos()
                    }
                    Log.d(
                        "@@@",
                        "mockLocation latitude:${mockLocation.latitude}, longitude:${mockLocation.longitude}"
                    )
                    setTestProviderLocation(provider, mockLocation)
                }
            }
            return SetMockLocationResult.Success
        } catch (e: Exception) {
            return SetMockLocationResult.Failure
        }
    }
}