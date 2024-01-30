package com.droidblossom.archive.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.core.app.ActivityCompat

class LocationUtil(private val context: Context) {
    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    fun getCurrentLocation(onLocationReceived: (Double, Double) -> Unit) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 권한이 없음
            return
        }

        val locationListener = LocationListener { location ->
            onLocationReceived(location.latitude, location.longitude)
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)

        val lastKnownLocationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (lastKnownLocationGPS != null) {
            onLocationReceived(lastKnownLocationGPS.latitude, lastKnownLocationGPS.longitude)
            locationManager.removeUpdates(locationListener)
            return
        }

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, locationListener)

        val lastKnownLocationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        if (lastKnownLocationNetwork != null) {
            onLocationReceived(lastKnownLocationNetwork.latitude, lastKnownLocationNetwork.longitude)
            locationManager.removeUpdates(locationListener)
        }
    }
}