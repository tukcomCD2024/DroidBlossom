package com.droidblossom.archive.presentation.ui.camera

import com.droidblossom.archive.domain.model.common.CapsuleMarker
import kotlinx.coroutines.flow.StateFlow

interface CameraViewModel {

    val capsuleList:StateFlow<List<CapsuleMarker>>

    fun getCapsules(latitude: Double, longitude: Double,): List<CapsuleMarker>
}