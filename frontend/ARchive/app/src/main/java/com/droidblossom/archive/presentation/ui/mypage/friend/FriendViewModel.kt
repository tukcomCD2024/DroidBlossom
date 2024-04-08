package com.droidblossom.archive.presentation.ui.mypage.friend

import com.droidblossom.archive.domain.model.friend.Friend
import com.droidblossom.archive.domain.model.friend.FriendsSearchResponse
import com.droidblossom.archive.presentation.ui.mypage.friend.addfriend.AddFriendViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface FriendViewModel {

    val friendEvent : SharedFlow<FriendEvent>


    val isFriendSearchOpen : StateFlow<Boolean>
    val friendList: StateFlow<List<Friend>>
    val friendListUI: StateFlow<List<Friend>>

    fun openSearchFriend()
    fun closeSearchFriend()
    fun searchFriend()
    fun getFriendList()
    fun changeDeleteOpen(previousPosition: Int?, currentPosition: Int)
    fun deleteFriend(friend : Friend)

    sealed class FriendEvent {
        data class ShowToastMessage(val message : String) : FriendViewModel.FriendEvent()
    }
}