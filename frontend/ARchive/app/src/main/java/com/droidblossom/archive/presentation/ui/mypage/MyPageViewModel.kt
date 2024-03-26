package com.droidblossom.archive.presentation.ui.mypage

import com.droidblossom.archive.domain.model.common.MyCapsule
import com.droidblossom.archive.domain.model.member.MemberDetail
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface MyPageViewModel {
    val myPageEvents : SharedFlow<MyPageEvent>

    val myInfo : StateFlow<MemberDetail>
    val myCapsules : StateFlow<List<MyCapsule>>
    val myCapsulesUI : StateFlow<List<MyCapsule>>
    val hasNextPage : StateFlow<Boolean>
    val lastCreatedTime : StateFlow<String>

    fun getMe()
    fun getSecretCapsulePage()
    fun clearCapsules()
    fun updateMyCapsulesUI()
    fun updateCapsuleOpenState(capsuleIndex: Int, capsuleId: Long)
    fun clickSetting()

    sealed class MyPageEvent {
        data class ShowToastMessage(val message : String) : MyPageEvent()

        data class CapsuleStateUpdate(val capsuleIndex: Int) : MyPageEvent()

        object ClickSetting : MyPageEvent()

    }
}