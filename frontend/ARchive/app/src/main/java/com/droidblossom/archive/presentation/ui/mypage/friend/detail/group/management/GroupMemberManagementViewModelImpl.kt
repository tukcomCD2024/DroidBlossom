package com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.management

import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class GroupMemberManagementViewModelImpl @Inject constructor(

) : BaseViewModel(), GroupMemberManagementViewModel{

    private val _groupId = MutableStateFlow(-1L)
    override val groupId: StateFlow<Long>
        get() = _groupId

    private val _groupMemberManagementEvents = MutableSharedFlow<GroupMemberManagementViewModel.GroupMemberManagementEvent>()
    override val groupMemberManagementEvents: SharedFlow<GroupMemberManagementViewModel.GroupMemberManagementEvent>
        get() = _groupMemberManagementEvents

    override fun groupDetailEvent(event: GroupMemberManagementViewModel.GroupMemberManagementEvent) {
        viewModelScope.launch {
            _groupMemberManagementEvents.emit(event)
        }
    }

    override fun setGroupId(groupId: Long) {
        _groupId.value = groupId
    }

}