package com.droidblossom.archive.presentation.ui.mypage.friendaccept

import com.droidblossom.archive.domain.model.friend.Friend
import com.droidblossom.archive.presentation.ui.mypage.friend.FriendViewModel
import com.droidblossom.archive.presentation.ui.social.page.friend.SocialFriendViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface FriendAcceptViewModel {

    val friendAcceptEvent: SharedFlow<FriendAcceptViewModel.FriendAcceptEvent>
    val friendAcceptList: StateFlow<List<Friend>>

    fun getFriendAcceptList()
    fun getLastedFriendAcceptList()
    fun onScrollNearBottom()
    fun denyRequest(friend: Friend)
    fun acceptRequest(friend: Friend)


    sealed class FriendAcceptEvent {
        data class ShowToastMessage(val message: String) : FriendAcceptEvent()
        object SwipeRefreshLayoutDismissLoading : FriendAcceptEvent()
    }
}