package com.droidblossom.archive.presentation.ui.mypage.friend

import com.droidblossom.archive.domain.model.friend.Friend
import com.droidblossom.archive.domain.model.friend.FriendsSearchResponse
import com.droidblossom.archive.domain.model.group.GroupSummary
import com.droidblossom.archive.presentation.ui.mypage.friend.addfriend.AddFriendViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface FriendViewModel {

    val friendEvent: SharedFlow<FriendEvent>

    //friend
    val isFriendSearchOpen: StateFlow<Boolean>
    val searchFriendText: MutableStateFlow<String>
    val friendList: StateFlow<List<Friend>>
    val friendListUI: StateFlow<List<Friend>>

    //group
    val isGroupSearchOpen: StateFlow<Boolean>
    val groupListUI: StateFlow<List<GroupSummary>>

    fun setEvent(event: FriendViewModel.FriendEvent)
    fun onScrollNearBottomFriend()
    fun onScrollNearBottomGroup()

    //friend
    fun openSearchFriend()
    fun closeSearchFriend()
    fun searchFriend()
    fun getFriendList()
    fun changeDeleteOpen(previousPosition: Int?, currentPosition: Int)
    fun deleteFriend(friend: Friend)

    //group
    fun openSearchGroup()
    fun closeSearchGroup()
    fun searchGroup()
    fun getGroupList()
    fun getGroupLastList()
    fun removeGroup(groupId: Long)

    sealed class FriendEvent {
        data class ShowToastMessage(val message: String) : FriendViewModel.FriendEvent()
        object OnRefreshEnd : FriendEvent()
    }
}