package com.droidblossom.archive.presentation.ui.mypage

import com.droidblossom.archive.domain.model.common.MyCapsule
import com.droidblossom.archive.domain.model.member.MemberDetail
import com.droidblossom.archive.presentation.model.mypage.CapsuleData
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface MyPageViewModel {
    val myPageEvents : SharedFlow<MyPageEvent>

    val myInfo : StateFlow<MemberDetail>
    val myCapsules : StateFlow<List<CapsuleData>>
    val myCapsulesUI : StateFlow<List<CapsuleData>>
    val hasNextPage : StateFlow<Boolean>
    val lastCreatedTime : StateFlow<String>
    var reloadMyInfo:Boolean

    fun getMe()
    fun getSecretCapsulePage()
    fun clearCapsules()
    fun updateMyCapsulesUI()
    fun updateCapsuleOpenState(capsuleIndex: Int, capsuleId: Long)
    fun clickSetting()

    fun load()
    sealed class MyPageEvent {
        data class ShowToastMessage(val message : String) : MyPageEvent()

        object ClickSetting : MyPageEvent()

    }
}