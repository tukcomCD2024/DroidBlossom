package com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.management

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface GroupMemberManagementViewModel {

    val groupId: StateFlow<Long>
    val groupMemberManagementEvents: SharedFlow<GroupMemberManagementEvent>

    fun groupDetailEvent(event: GroupMemberManagementEvent)
    fun setGroupId(groupId: Long)

    sealed class GroupMemberManagementEvent{
        data class ShowToastMessage(val message : String) : GroupMemberManagementEvent()
    }
}