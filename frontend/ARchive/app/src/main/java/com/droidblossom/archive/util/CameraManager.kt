package com.droidblossom.archive.util

import android.hardware.Camera
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object CameraManager {
    private var camera: Camera? = null

    suspend fun getCameraInstance(): Camera? = withContext(Dispatchers.IO) {
        if (camera == null) {
            try {
                camera = Camera.open()
            } catch (e: Exception) {
                // 오류 처리
            }
        }
        camera
    }

    suspend fun releaseCamera() = withContext(Dispatchers.IO) {
        camera?.apply {
            stopPreview()
            release()
            camera = null
        }
    }
}