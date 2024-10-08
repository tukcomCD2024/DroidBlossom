package com.droidblossom.archive.presentation.ui.mypage.friend.detail.friend

import com.droidblossom.archive.presentation.model.mypage.CapsuleData
import com.droidblossom.archive.presentation.model.mypage.detail.FriendProfileData
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface FriendDetailViewModel {

    val friendInfo : StateFlow<FriendProfileData>
    val friendDetailEvents: SharedFlow<FriendDetailEvent>
    val capsules : StateFlow<List<CapsuleData>>
    val hasNextPage : StateFlow<Boolean>
    val lastCreatedTime : StateFlow<String>

    fun friendDetailEvent(event: FriendDetailEvent)

    fun onScrollNearBottom()

    fun getCapsuleList()
    fun getLatestCapsuleList()


    sealed class FriendDetailEvent{
        object SwipeRefreshLayoutDismissLoading : FriendDetailEvent()
        data class ShowToastMessage(val message : String) : FriendDetailEvent()
    }
}