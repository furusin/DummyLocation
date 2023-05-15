@file:OptIn(ExperimentalMaterial3Api::class)

package net.furusin.www.dummylocation

import android.Manifest
import android.app.AppOpsManager
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import net.furusin.www.dummylocation.composable.Main
import net.furusin.www.dummylocation.ui.theme.DummyLocationTheme

@RequiresApi(Build.VERSION_CODES.S)
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    private val locationManager: LocationManager by lazy {
        getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (isMockAppEnabled(packageName).not()) {
            Toast.makeText(this, "App has not set as mock location app", Toast.LENGTH_LONG).show()
        }

        setContent {
            DummyLocationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Main(
                        viewModel = viewModel,
                        setButtonClicked = {
                            val toastMessage = when (viewModel.setMockLocation(locationManager)) {
                                SetMockLocationResult.Success -> "Location Successfully set"
                                SetMockLocationResult.Failure -> "Location set failed"
                            }
                            Toast.makeText(
                                this,
                                toastMessage,
                                Toast.LENGTH_LONG
                            ).show()
                        },
                        checkButtonClicked = {
                            val mockedLocation = getMockedLocation() ?: return@Main
                            Toast.makeText(
                                this,
                                "Mocked Location Longitude:${mockedLocation.longitude} Latitude:${mockedLocation.latitude}",
                                Toast.LENGTH_LONG
                            ).show()
                        })
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> getMockedLocation()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun isMockAppEnabled(packageName: String): Boolean {
        try {
            val opsManager = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            return opsManager.checkOp(
                AppOpsManager.OPSTR_MOCK_LOCATION,
                android.os.Process.myUid(),
                packageName
            ) == AppOpsManager.MODE_ALLOWED
        } catch (e: SecurityException) {
            return false
        }
    }

    private fun getMockedLocation(): Location? {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE
            )
            return null
        }

        val criteria = Criteria().apply { accuracy = Criteria.ACCURACY_FINE }
        val provider = locationManager.getBestProvider(criteria, true) ?: return null
        val mockedLocation = locationManager.getLastKnownLocation(provider) ?: return null

        return mockedLocation
    }


    private companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}