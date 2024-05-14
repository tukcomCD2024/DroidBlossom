package com.droidblossom.archive.presentation.ui.camera

import com.droidblossom.archive.domain.model.capsule.CapsuleAnchor
import io.github.sceneview.ar.node.AnchorNode
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface CameraViewModel {

    val selectedCapsuleFilter : StateFlow<CapsuleFilterType>
    val capsuleList:StateFlow<List<CapsuleAnchor>>
    val cameraEvents: SharedFlow<CameraEvent>
    var capsuleListSize: Int
    val anchorNodes: StateFlow<List<AnchorNode>>
    val isFriendsCapsuleDisplay : StateFlow<Boolean>
    val isCapsulesAdded: Boolean

    fun selectFilter(capsuleFilterType: CapsuleFilterType, latitude: Double, longitude: Double)
    fun getCapsules(latitude: Double, longitude: Double)

    fun cameraEvent(event : CameraEvent)

    fun addAnchorNode(anchorNode: AnchorNode)

    fun clearAnchorNode()


    sealed class CameraEvent{
        data class ShowCapsulePreviewDialog(val capsuleId: String, val capsuleType: String) : CameraEvent()
        object ShowLoading : CameraEvent()
        object DismissLoading : CameraEvent()
    }

    enum class CapsuleFilterType(val description: String){
        FILTER_ALL("ALL"),
        FILTER_SECRET("SECRET"),
        FILTER_GROUP("GROUP"),
        FILTER_PUBLIC_MY("PUBLIC"),
        FILTER_PUBLIC_FRIEND("PUBLIC_FRIEND")
    }
}