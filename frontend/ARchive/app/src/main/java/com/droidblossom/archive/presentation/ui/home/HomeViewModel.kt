package com.droidblossom.archive.presentation.ui.home

import com.droidblossom.archive.domain.model.common.CapsuleMarker
import com.droidblossom.archive.presentation.ui.auth.AuthViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface HomeViewModel {

    val filterCapsuleSelect : StateFlow<CapsuleFilter>
    val isClickedFAB : StateFlow<Boolean>
    val existsNotification : StateFlow<Boolean>
    val followLocation : StateFlow<Boolean>
    val capsuleList: StateFlow<List<CapsuleMarker>>

    val homeEvents: SharedFlow<HomeEvent>

    fun selectPublic()
    fun selectGroup()
    fun selectSecret()
    fun selectHotPlace()
    fun clickFollowBtn()
    fun clickNotificationBtn()
    fun clickFAB()

    fun resetNearbyCapsules()
    fun getNearbyCapsules(latitude: Double, longitude: Double, distance: Double, capsuleType: String)

    fun homeEvent(event:HomeEvent)

    enum class CapsuleFilter{
        ALL,
        SECRET,
        PUBLIC,
        GROUP,
        HOT;
        override fun toString(): String {
            return when (this) {
                SECRET, PUBLIC, GROUP -> this.name
                else -> "ALL"
            }
        }
    }

    sealed class HomeEvent{
        object ShowCapsulePreviewDialog : HomeEvent()
    }
}