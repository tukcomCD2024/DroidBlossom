package com.droidblossom.archive.presentation.ui.mypage.friend

import com.droidblossom.archive.domain.model.friend.Friend
import com.droidblossom.archive.domain.model.friend.FriendsSearchResponse
import kotlinx.coroutines.flow.StateFlow

interface FriendViewModel {

    val isFriendSearchOpen : StateFlow<Boolean>
    val friendList: StateFlow<List<Friend>>
    val friendListUI: StateFlow<List<Friend>>

    fun openSearchFriend()
    fun closeSearchFriend()
    fun searchFriend()
    fun getFriendList()
    fun changeDeleteOpen(previousPosition: Int?, currentPosition: Int)

}