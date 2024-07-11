package com.droidblossom.archive.presentation.ui.mypage

import com.droidblossom.archive.domain.model.member.MemberDetail
import com.droidblossom.archive.presentation.model.mypage.CapsuleData
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface MyPageViewModel {
    val myPageEvents : SharedFlow<MyPageEvent>

    val myInfo : StateFlow<MemberDetail>
    val myCapsules : StateFlow<List<CapsuleData>>
    val hasNextPage : StateFlow<Boolean>
    val lastCreatedTime : StateFlow<String>
    val capsuleType: StateFlow<MyPageFragment.SpinnerCapsuleType>
    var viewModelReload:Boolean

    fun getMe()
    fun updateCapsuleOpenState(capsuleIndex: Int, capsuleId: Long)
    fun clickSetting()
    fun getCapsulePage()
    fun getLatestCapsulePage()
    fun load()
    fun selectSpinnerItem(item:MyPageFragment.SpinnerCapsuleType)
    fun myPageEvent(event: MyPageEvent)
    fun onScrollNearBottom()

    fun deleteCapsule(capsuleIndex: Int, capsuleId: Long)

    sealed class MyPageEvent {
        data class ShowToastMessage(val message : String) : MyPageEvent()

        object SwipeRefreshLayoutDismissLoading : MyPageEvent()

        object ClickSetting : MyPageEvent()

    }
}