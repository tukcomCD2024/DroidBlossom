package com.droidblossom.archive.presentation.ui.camera

import com.droidblossom.archive.domain.model.common.CapsuleMarker
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface CameraViewModel {

    val capsuleList:StateFlow<List<CapsuleMarker>>
    val cameraEvents: SharedFlow<CameraEvent>

    fun getCapsules(latitude: Double, longitude: Double,): List<CapsuleMarker>
    fun cameraEvent(event : CameraEvent)

    sealed class CameraEvent{
        data class ShowCapsulePreviewDialog(val capsuleId: String, val capsuleType: String) : CameraEvent()
    }
}