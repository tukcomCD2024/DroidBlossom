package com.droidblossom.archive.presentation.ui.camera

import com.droidblossom.archive.domain.model.capsule.CapsuleAnchor
import io.github.sceneview.ar.node.AnchorNode
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface CameraViewModel {

    val capsuleList:StateFlow<List<CapsuleAnchor>>
    val cameraEvents: SharedFlow<CameraEvent>
    val capsuleListSize: SharedFlow<Int>
    val anchorNodes: StateFlow<List<AnchorNode>>

    fun getCapsules(latitude: Double, longitude: Double)

    fun cameraEvent(event : CameraEvent)

    fun addAnchorNode(anchorNode: AnchorNode)

    fun clearAnchorNode()

    sealed class CameraEvent{
        data class ShowCapsulePreviewDialog(val capsuleId: String, val capsuleType: String) : CameraEvent()
        object DismissLoading : CameraEvent()

    }
}