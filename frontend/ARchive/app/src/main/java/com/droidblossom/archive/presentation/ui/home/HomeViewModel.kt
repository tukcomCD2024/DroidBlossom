package com.droidblossom.archive.presentation.ui.home

import com.droidblossom.archive.presentation.ui.auth.AuthViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface HomeViewModel {

    val filterCapsuleSelect : StateFlow<CapsuleFilter>
    val isClickedFAB : StateFlow<Boolean>
    val existsNotification : StateFlow<Boolean>
    val followLocation : StateFlow<Boolean>

    val homeEvents: SharedFlow<HomeEvent>

    fun selectPublic()
    fun selectGroup()
    fun selectSecret()
    fun selectHotPlace()
    fun clickFollowBtn()
    fun clickNotificationBtn()
    fun clickFAB()

    fun homeEvent(event:HomeEvent)

    enum class CapsuleFilter{
        ALL,
        SECRET,
        PUBLIC,
        GROUP,
        HOT
    }

    sealed class HomeEvent{
        object ShowCapsulePreviewDialog : HomeEvent()
    }
}