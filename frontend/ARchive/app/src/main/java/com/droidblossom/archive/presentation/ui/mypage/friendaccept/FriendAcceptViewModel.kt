package com.droidblossom.archive.presentation.ui.mypage.friendaccept

import com.droidblossom.archive.domain.model.friend.Friend
import com.droidblossom.archive.domain.model.group.GroupInviteSummary
import com.droidblossom.archive.presentation.ui.mypage.friend.FriendViewModel
import com.droidblossom.archive.presentation.ui.social.page.friend.SocialFriendViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface FriendAcceptViewModel {

    val friendAcceptEvent: SharedFlow<FriendAcceptViewModel.FriendAcceptEvent>
    val friendAcceptList: StateFlow<List<Friend>>
    val friendSendAcceptList: StateFlow<List<Friend>>
    val groupAcceptList : StateFlow<List<GroupInviteSummary>>

    fun getFriendAcceptListPage()
    fun getLastedFriendAcceptList()
    fun onScrollFriendNearBottom()
    fun denyFriendRequest(friend: Friend)
    fun acceptFriendRequest(friend: Friend)

    fun getGroupAcceptListPage()
    fun getLastedGroupAcceptList()
    fun onScrollGroupNearBottom()
    fun denyGroupRequest(group: GroupInviteSummary)
    fun acceptGroupRequest(group: GroupInviteSummary)

    fun getFriendSendAcceptListPage()
    fun onScrollFriendSendNearBottom()
    fun deleteFriendSendRequest(friend: Friend)

    sealed class FriendAcceptEvent {
        data class ShowToastMessage(val message: String) : FriendAcceptEvent()
        object SwipeRefreshLayoutDismissLoading : FriendAcceptEvent()
    }
}