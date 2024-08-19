package com.droidblossom.archive.presentation.ui.home

import com.droidblossom.archive.domain.model.capsule.CapsuleMarker
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface HomeViewModel {

    val filterCapsuleSelect : StateFlow<CapsuleFilter>
    val isClickedFAB : StateFlow<Boolean>
    val existsNotification : StateFlow<Boolean>
    val followLocation : StateFlow<Boolean>
    val isFriendsCapsuleDisplay : StateFlow<Boolean>
    val capsuleList: StateFlow<List<CapsuleMarker>>

    val homeEvents: SharedFlow<HomeEvent>

    fun selectPublic()
    fun selectGroup()
    fun selectSecret()
    fun selectTreasure()
    fun clickFollowBtn()
    fun clickNotificationBtn()
    fun clickFAB()
    fun clickFriendsDisplay()

    fun resetNearbyCapsules()
    fun getNearbyMyCapsules(latitude: Double, longitude: Double, distance: Double, capsuleType: String)
    fun getNearbyFriendsCapsules(latitude: Double, longitude: Double, distance: Double)
    fun getNearbyMyAndFriendsCapsules(latitude: Double, longitude: Double, distance: Double, capsuleType: String)


    fun homeEvent(event:HomeEvent)

    enum class CapsuleFilter{
        ALL,
        SECRET,
        PUBLIC,
        GROUP,
        TREASURE;
        override fun toString(): String {
            return when (this) {
                SECRET, PUBLIC, GROUP, TREASURE -> this.name
                else -> "ALL"
            }
        }
    }

    sealed class HomeEvent{
        data class ShowToast(val message: String): HomeEvent()
        object GoNotification : HomeEvent()
        data class ShowCapsulePreviewDialog(val capsuleId: String, val capsuleType: String) : HomeEvent()
    }
}