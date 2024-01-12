package com.droidblossom.archive.presentation.ui.home

import kotlinx.coroutines.flow.StateFlow

interface HomeViewModel {

    val filterCapsuleSelect : StateFlow<CapsuleFilter>
    val isClickedFAB : StateFlow<Boolean>
    val existsNotification : StateFlow<Boolean>
    val followLocation : StateFlow<Boolean>


    fun selectPublic()
    fun selectGroup()
    fun selectSecret()
    fun selectHotPlace()
    fun clickFollowBtn()
    fun clickNotificationBtn()
    fun clickFAB()
    enum class CapsuleFilter{
        ALL,
        SECRET,
        PUBLIC,
        GROUP,
        HOT
    }
}