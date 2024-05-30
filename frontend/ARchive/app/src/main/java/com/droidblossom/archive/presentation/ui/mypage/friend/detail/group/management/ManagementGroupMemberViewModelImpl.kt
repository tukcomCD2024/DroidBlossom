package com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.management

import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.domain.model.group.GroupMember
import com.droidblossom.archive.presentation.base.BaseViewModel
import com.droidblossom.archive.presentation.model.mypage.detail.FriendForGroupInvite
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ManagementGroupMemberViewModelImpl @Inject constructor(

) : BaseViewModel(), ManagementGroupMemberViewModel{

    private val _groupId = MutableStateFlow(-1L)
    override val groupId: StateFlow<Long>
        get() = _groupId

    private val _managementGroupMemberEvents = MutableSharedFlow<ManagementGroupMemberViewModel.ManagementGroupMemberEvent>()
    override val managementGroupMemberEvents: SharedFlow<ManagementGroupMemberViewModel.ManagementGroupMemberEvent>
        get() = _managementGroupMemberEvents

    private val _groupMembers = MutableStateFlow(listOf<GroupMember>())
    override val groupMembers: StateFlow<List<GroupMember>>
        get() = _groupMembers

    private val _invitedUsers = MutableStateFlow(listOf<GroupMember>())
    override val invitedUsers: StateFlow<List<GroupMember>>
        get() = _invitedUsers

    private val _invitableFriends = MutableStateFlow(listOf<FriendForGroupInvite>())
    override val invitableFriends: StateFlow<List<FriendForGroupInvite>>
        get() = _invitableFriends
    override fun managementGroupMemberEvent(event: ManagementGroupMemberViewModel.ManagementGroupMemberEvent) {
        viewModelScope.launch {
            _managementGroupMemberEvents.emit(event)
        }
    }

    override fun setGroupId(groupId: Long) {
        _groupId.value = groupId
    }

}