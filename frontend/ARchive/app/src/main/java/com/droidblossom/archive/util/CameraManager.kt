package com.droidblossom.archive.util

import android.hardware.Camera
import android.util.Log


object CameraManager {
    private var camera: Camera? = null

    fun getCameraInstance(): Camera? {
        if (camera == null) {
            try {
                camera = Camera.open()
                Log.d("카메라","카메라 오픈")
            } catch (e: Exception) {
            }
        }
        return camera
    }

    fun releaseCamera() {
        camera?.apply {
            stopPreview()
            release()
            Log.d("카메라","카메라 오프")
            camera = null
        }
    }
}