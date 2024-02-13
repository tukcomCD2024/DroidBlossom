package com.droidblossom.archive.util

import android.app.Activity
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionsUtil(private val activity: Activity) {
    companion object {
        const val REQUEST_LOCATION_PERMISSION = 101
        const val REQUEST_CAMERA_PERMISSION = 102
        const val REQUEST_CONTACTS_PERMISSION = 103
        const val POST_NOTIFICATIONS = 104
        const val REQUEST_ALL_PERMISSIONS = 105
    }

    fun requestAllPermissions() {
        val permissions = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_CONTACTS
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        requestPermissions(permissions.toTypedArray(), REQUEST_ALL_PERMISSIONS)
    }

    fun areEssentialPermissionsGranted(): Boolean {
        val essentialPermissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
        )

        return essentialPermissions.all { permission ->
            ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
        }
    }


    fun requestLocationPermissions() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_LOCATION_PERMISSION)
    }

    fun hasLocationPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    fun requestCameraPermission() {
        requestPermission(Manifest.permission.CAMERA, REQUEST_CAMERA_PERMISSION)
    }

    fun requestContactsPermission() {
        requestPermission(Manifest.permission.READ_CONTACTS, REQUEST_CONTACTS_PERMISSION)
    }


    private fun requestPermissions(permissions: Array<String>, requestCode: Int) {
        val shouldProvideRationale = permissions.any { ActivityCompat.shouldShowRequestPermissionRationale(activity, it) }

        if (shouldProvideRationale) {
            // 여기서 사용자에게 권한 요청의 필요성에 대해 설명하고, 권한 요청을 다시 진행할 수 있도록 유도
        } else {
            ActivityCompat.requestPermissions(activity, permissions, requestCode)
        }
    }

    private fun requestPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)
        }
    }

    fun handleRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray, permissionGranted: (Int) -> Unit, permissionDenied: (Int) -> Unit) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            permissionGranted(requestCode)
        } else {
            permissionDenied(requestCode)
        }
    }
}
