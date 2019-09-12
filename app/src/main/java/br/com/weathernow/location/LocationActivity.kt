package br.com.weathernow.location

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Location
import android.net.Uri.fromParts
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import br.com.weathernow.BaseActivity
import br.com.weathernow.R
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

abstract class LocationActivity : BaseActivity() {

    private val googleApiAvailability = GoogleApiAvailability.getInstance()
    private var onLocationPermissionGranted: () -> Unit = {}
    private var fusedLocationClient: FusedLocationProviderClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkGooglePlayServices()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_PLAY_SERVICES_CODE) {
            checkGooglePlayServices()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED)) {
                onLocationPermissionGranted()
            } else {
                showLocationPermissionPermanentlyDeniedUi()
            }
        }
    }

    // Abstract methods

    abstract fun showGooglePlayServicesCanceledUi()

    abstract fun showLocationPermissionDeniedUi()

    abstract fun showLocationPermissionPermanentlyDeniedUi()

    // Protected methods

    protected fun checkPermissionAndGetLastKnownLocation(listener: LatLongListener) {
        onLocationPermissionGranted = { getLastKnownLocation(listener) }
        checkLocationPermission()
    }

    protected fun checkGooglePlayServices() {
        val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this)
        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                onGoogleServicesResolvableError(resultCode)
            } else {
                onGoogleServicesNonResolvableError()
            }
        }
    }

    protected fun goToSettings() {
        val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, REQUEST_PERMISSION_SETTING_CODE)
    }

    // Private methods

    private fun onGoogleServicesResolvableError(errorCode: Int) {
        googleApiAvailability.getErrorDialog(this, errorCode, GOOGLE_PLAY_SERVICES_CODE) {
            onGoogleServicesErrorActionCanceled()
        }
    }

    private fun onGoogleServicesErrorActionCanceled() {
        showGooglePlayServicesCanceledUi()
    }

    private fun onGoogleServicesNonResolvableError() {
        showAlertDialog(
            title = getString(R.string.google_play_services_action_non_resolvable_error_dialog_title),
            message = getString(R.string.google_play_services_action_non_resolvable_error_dialog_message),
            cancelable = false
        ) {
            finish()
        }
    }

    private fun checkLocationPermission() {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        if (ContextCompat.checkSelfPermission(this, permission) == PERMISSION_GRANTED) {
            onLocationPermissionGranted()
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                showRationale()
            } else {
                requestLocationPermission()
            }
        }
    }

    private fun showRationale() {
        showAlertDialog(
            title = getString(R.string.location_permission_rationale_dialog_title),
            message = getString(R.string.location_permission_rationale_dialog_message),
            positiveButtonLabel = getString(R.string.yes),
            positiveButtonAction = { requestLocationPermission() },
            negativeButtonLabel = getString(R.string.no),
            negativeButtonAction = { showLocationPermissionDeniedUi() }
        )
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_CODE
        )
    }

    private fun getLastKnownLocation(listener: LatLongListener) {
        initializeLocationClientIfNeeded()
        fusedLocationClient?.lastLocation?.addOnSuccessListener { location: Location? ->
            if (location != null) {
                listener.onLatLongReady(location.latitude, location.longitude)
            } else {
                listener.onLastKnownLocationError()
            }
        }
        fusedLocationClient?.lastLocation?.addOnFailureListener {
            listener.onLastKnownLocationError()
        }
    }

    private fun initializeLocationClientIfNeeded() {
        if (fusedLocationClient == null) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        }
    }

    interface LatLongListener {
        fun onLatLongReady(latitude: Double, longitude: Double)
        fun onLastKnownLocationError()
    }

    companion object {
        private const val GOOGLE_PLAY_SERVICES_CODE = 1
        private const val LOCATION_PERMISSION_CODE = 2
        const val REQUEST_PERMISSION_SETTING_CODE = 3
    }

}