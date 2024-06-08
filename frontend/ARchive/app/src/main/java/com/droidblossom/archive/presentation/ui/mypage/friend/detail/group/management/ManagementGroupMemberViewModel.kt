package com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.management

import com.droidblossom.archive.domain.model.group.GroupInvitedUser
import com.droidblossom.archive.domain.model.group.GroupMember
import com.droidblossom.archive.presentation.model.mypage.detail.FriendForGroupInvite
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface ManagementGroupMemberViewModel {

    val groupId: StateFlow<Long>
    val managementGroupMemberEvents: SharedFlow<ManagementGroupMemberEvent>
    val groupMembers: StateFlow<List<GroupMember>>
    val invitedUsers: StateFlow<List<GroupInvitedUser>>
    val invitedUsersHasNextPage: StateFlow<Boolean>
    val invitableFriends: StateFlow<List<FriendForGroupInvite>>
    val groupInviteeList: StateFlow<List<FriendForGroupInvite>>
    val invitableFriendsHasNextPage: StateFlow<Boolean>
    val invitableFriendsLastCreatedTime: StateFlow<String>

    var remainingInvites : Int

    fun managementGroupMemberEvent(event: ManagementGroupMemberEvent)
    fun setGroupId(groupId: Long)

    fun getGroupMemberList()

    fun getInvitableFriendList()
    fun getLatestInvitableFriendList()
    fun onInvitableFriendsRVNearBottom()

    fun getInvitedUserList()
    fun getLatestInvitedUserList()
    fun onInvitedUsersRVScrollNearBottom()

    fun onClickInvitableFriend(position: Int)

    fun inviteFriendsToGroup()

    sealed class ManagementGroupMemberEvent{
        data class ShowToastMessage(val message : String) : ManagementGroupMemberEvent()
        data class NavigateToPage(val page: Int) : ManagementGroupMemberEvent()
        object SwipeRefreshLayoutDismissLoading : ManagementGroupMemberEvent()
    }
}