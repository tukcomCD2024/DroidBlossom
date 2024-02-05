package com.droidblossom.archive.presentation.ui.mypage

import com.droidblossom.archive.domain.model.common.MyCapsule
import com.droidblossom.archive.domain.model.member.MemberDetail
import com.droidblossom.archive.presentation.base.BaseViewModel
import com.droidblossom.archive.presentation.ui.home.createcapsule.CreateCapsuleViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface MyPageViewModel {
    val myPageEvents : SharedFlow<MyPageEvent>

    val myInfo : StateFlow<MemberDetail>
    val myCapsules : StateFlow<List<MyCapsule>>
    val hasNextPage : StateFlow<Boolean>
    val lastCreatedTime : StateFlow<String>

    fun getMe()
    fun getSecretCapsulePage()
    fun clearCapsules()

    sealed class MyPageEvent {
        data class ShowToastMessage(val message : String) : MyPageEvent()
    }
}