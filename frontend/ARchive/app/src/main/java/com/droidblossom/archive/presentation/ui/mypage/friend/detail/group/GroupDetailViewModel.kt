package com.droidblossom.archive.presentation.ui.mypage.friend.detail.group

import com.droidblossom.archive.presentation.model.mypage.CapsuleData
import com.droidblossom.archive.presentation.model.mypage.GroupProfileData
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface GroupDetailViewModel {

    val groupInfo: StateFlow<GroupProfileData>
    val groupDetailEvents: SharedFlow<GroupDetailEvent>
    val capsules : StateFlow<List<CapsuleData>>
    val capsulesHasNextPage : StateFlow<Boolean>
    val capsulesLastCreatedTime : StateFlow<String>
    val isShowMore:StateFlow<Boolean>

    fun groupDetailEvent(event: GroupDetailEvent)

    fun onCapsuleScrollNearBottom()

    fun getCapsuleList()
    fun getLatestCapsuleList()
    fun setShowMore()

    sealed class GroupDetailEvent{
        object SwipeRefreshLayoutDismissLoading : GroupDetailEvent()
        data class ShowToastMessage(val message : String) : GroupDetailEvent()
    }
}