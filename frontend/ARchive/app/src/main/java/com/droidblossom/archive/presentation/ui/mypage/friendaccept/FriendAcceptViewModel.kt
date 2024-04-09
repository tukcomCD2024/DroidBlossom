package com.droidblossom.archive.presentation.ui.mypage.friendaccept

import com.droidblossom.archive.presentation.ui.mypage.friend.FriendViewModel
import kotlinx.coroutines.flow.SharedFlow

interface FriendAcceptViewModel {

    val friendAcceptEvent : SharedFlow<FriendViewModel.FriendEvent>


    sealed class FriendAcceptEvent {
        data class ShowToastMessage(val message : String) : FriendAcceptEvent()
    }
}