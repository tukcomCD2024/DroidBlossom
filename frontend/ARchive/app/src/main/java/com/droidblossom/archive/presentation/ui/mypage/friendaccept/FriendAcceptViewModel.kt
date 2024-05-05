package com.droidblossom.archive.presentation.ui.mypage.friendaccept

import com.droidblossom.archive.domain.model.friend.Friend
import com.droidblossom.archive.presentation.ui.mypage.friend.FriendViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface FriendAcceptViewModel {

    val friendAcceptEvent: SharedFlow<FriendViewModel.FriendEvent>
    val friendAcceptList: StateFlow<List<Friend>>

    fun getFriendAcceptList()
    fun onScrollNearBottom()
    fun denyRequest(friend: Friend)
    fun acceptRequest(friend: Friend)


    sealed class FriendAcceptEvent {
        data class ShowToastMessage(val message: String) : FriendAcceptEvent()
    }
}