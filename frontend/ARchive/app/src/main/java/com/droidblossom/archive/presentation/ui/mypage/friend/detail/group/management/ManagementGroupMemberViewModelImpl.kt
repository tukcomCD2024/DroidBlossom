package com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.management

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.data.dto.common.IdBasedPagingRequestDto
import com.droidblossom.archive.data.dto.common.PagingRequestDto
import com.droidblossom.archive.data.dto.group.request.InviteGroupRequestDto
import com.droidblossom.archive.domain.model.group.GroupInvitedUser
import com.droidblossom.archive.domain.model.group.GroupMember
import com.droidblossom.archive.domain.usecase.friend.FriendForGroupInvitePageUseCase
import com.droidblossom.archive.domain.usecase.group.GetGroupInvitedUsersUseCase
import com.droidblossom.archive.domain.usecase.group.GetGroupMembersInfoUseCase
import com.droidblossom.archive.domain.usecase.group.GroupInviteUseCase
import com.droidblossom.archive.presentation.base.BaseViewModel
import com.droidblossom.archive.presentation.model.mypage.detail.FriendForGroupInvite
import com.droidblossom.archive.util.DateUtils
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltViewModel
class ManagementGroupMemberViewModelImpl @Inject constructor(
    private val friendForGroupInvitePageUseCase: FriendForGroupInvitePageUseCase,
    private val groupInviteUseCase: GroupInviteUseCase,
    private val getGroupMembersInfoUseCase: GetGroupMembersInfoUseCase,
    private val getGroupInvitedUsersUseCase: GetGroupInvitedUsersUseCase
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

    private val _invitedUsers = MutableStateFlow(listOf<GroupInvitedUser>())
    override val invitedUsers: StateFlow<List<GroupInvitedUser>>
        get() = _invitedUsers

    private val _invitedUsersHasNextPage = MutableStateFlow(true)
    override val invitedUsersHasNextPage: StateFlow<Boolean>
        get() = _invitedUsersHasNextPage

    private val _invitableFriends = MutableStateFlow(listOf<FriendForGroupInvite>())
    override val invitableFriends: StateFlow<List<FriendForGroupInvite>>
        get() = _invitableFriends

    private val _groupInviteeList = MutableStateFlow(listOf<FriendForGroupInvite>())

    override val groupInviteeList: StateFlow<List<FriendForGroupInvite>>
        get() = _groupInviteeList


    private val _invitableFriendsHasNextPage = MutableStateFlow(false)
    override val invitableFriendsHasNextPage: StateFlow<Boolean>
        get() = _invitableFriendsHasNextPage

    private val _invitableFriendsCreatedTime = MutableStateFlow(DateUtils.dataServerString)

    override val invitableFriendsLastCreatedTime: StateFlow<String>
        get() = _invitableFriendsCreatedTime

    override var remainingInvites = -1

    override fun managementGroupMemberEvent(event: ManagementGroupMemberViewModel.ManagementGroupMemberEvent) {
        viewModelScope.launch {
            _managementGroupMemberEvents.emit(event)
        }
    }

    private val invitableFriendsRVScrollEventChannel = Channel<Unit>(Channel.CONFLATED)
    private val invitableFriendsRVScrollEventFlow =
        invitableFriendsRVScrollEventChannel.receiveAsFlow().throttleFirst(1000, TimeUnit.MILLISECONDS)

    private var getInvitableFriendListJob: Job? = null

    private val invitedUsersRVScrollEventChannel = Channel<Unit>(Channel.CONFLATED)
    private val invitedUsersRVScrollEventFlow =
        invitedUsersRVScrollEventChannel.receiveAsFlow().throttleFirst(1000, TimeUnit.MILLISECONDS)

    private var getInvitedUserListJob: Job? = null

    init {
        viewModelScope.launch {
            invitableFriendsRVScrollEventFlow.collect {
                if (invitableFriends.value.isEmpty()) {
                    getLatestInvitableFriendList()
                } else {
                    getInvitableFriendList()
                }
            }
        }

        viewModelScope.launch {
            invitedUsersRVScrollEventFlow.collect {
                if (invitedUsers.value.isEmpty()) {
                    getLatestInvitedUserList()
                } else {
                    getInvitedUserList()
                }
            }
        }
    }

    fun load(){
        getLatestInvitableFriendList()
        getLatestInvitedUserList()
        getGroupMemberList()
    }

    override fun onInvitableFriendsRVNearBottom() {
        invitableFriendsRVScrollEventChannel.trySend(Unit)
    }

    override fun onInvitedUsersRVScrollNearBottom() {
        invitedUsersRVScrollEventChannel.trySend(Unit)
    }


    override fun setGroupId(groupId: Long) {
        _groupId.value = groupId
        load()
    }

    override fun getGroupMemberList(){
        viewModelScope.launch {
            getGroupMembersInfoUseCase(groupId.value).collect{ result ->
                result.onSuccess {
                    _groupMembers.value = it.groupMemberResponses
                }.onFail {

                }
            }
        }
    }

    override fun inviteFriendsToGroup(){
        // 성공한다면?
        viewModelScope.launch {
            val targetIds = groupInviteeList.value.map { it.id }
            groupInviteUseCase(
                InviteGroupRequestDto(
                    groupId.value,
                    targetIds
                )
            ).collect{ result ->
                result.onSuccess {
                    // 다른 api 호출을 해서 동기화 or list 조작
                    // 타입 맞춰야하는데 api 나오면 할 예정
                    //_invitedUsers.value += _groupInviteeList.value
                    // _groupInviteeList.value = emptyList()
                    load()
                }.onFail {
                    // Toast와 다시 계산
                }
            }
        }
    }

    override fun getInvitableFriendList(){
        if (invitableFriendsHasNextPage.value){
            getInvitableFriendListJob?.cancel()
            getInvitableFriendListJob = viewModelScope.launch {
                friendForGroupInvitePageUseCase(
                    groupId = groupId.value,
                    pagingRequestDto = PagingRequestDto(
                        15,
                        invitableFriendsLastCreatedTime.value
                    )
                ).collect{ result ->
                    result.onSuccess {
                        _invitableFriendsHasNextPage.value = it.hasNext
                        _invitableFriends.value =  invitableFriends.value + it.friends
                        if (invitableFriends.value.isNotEmpty()){
                            _invitableFriendsCreatedTime.value = it.friends.last().createdAt
                        }
                    }.onFail {
                        managementGroupMemberEvent(ManagementGroupMemberViewModel.ManagementGroupMemberEvent.ShowToastMessage("초대 가능한 친구 불러오기 실패"))
                    }
                }
            }
        }

    }
    override fun getLatestInvitableFriendList(){
        _groupInviteeList.value = emptyList()
        getInvitableFriendListJob?.cancel()
        getInvitableFriendListJob = viewModelScope.launch {
            friendForGroupInvitePageUseCase(
                groupId = groupId.value,
                pagingRequestDto = PagingRequestDto(
                    15,
                    DateUtils.dataServerString
                )
            ).collect{ result ->
                result.onSuccess {
                    _invitableFriendsHasNextPage.value = it.hasNext
                    _invitableFriends.value = it.friends
                    if (invitableFriends.value.isNotEmpty()){
                        _invitableFriendsCreatedTime.value = it.friends.last().createdAt
                    }
                }.onFail {
                    managementGroupMemberEvent(ManagementGroupMemberViewModel.ManagementGroupMemberEvent.ShowToastMessage("초대 가능한 친구 불러오기 실패"))
                }
            }
            managementGroupMemberEvent(ManagementGroupMemberViewModel.ManagementGroupMemberEvent.SwipeRefreshLayoutDismissLoading)
        }
    }

    override fun onClickInvitableFriend(position: Int){
        if (_invitableFriends.value[position].isChecked){
            _invitableFriends.value[position].isChecked = false
            _groupInviteeList.value -= invitableFriends.value[position]
        }else{
            if (remainingInvites >= invitableFriends.value.size){
                _invitableFriends.value[position].isChecked = true
                _groupInviteeList.value += invitableFriends.value[position]
            }else{
                managementGroupMemberEvent(ManagementGroupMemberViewModel.ManagementGroupMemberEvent.ShowToastMessage("현재 ${remainingInvites}명까지 초대 가능합니다."))
            }
        }
    }

    override fun getInvitedUserList(){
        if (invitedUsersHasNextPage.value){
            getInvitedUserListJob = viewModelScope.launch {
                getGroupInvitedUsersUseCase(
                    groupId = groupId.value,
                    pagingRequest = IdBasedPagingRequestDto(
                        size = 15,
                        pagingId = invitedUsers.value.lastOrNull()?.groupInviteId
                    )
                ).collect{ result ->
                    result.onSuccess {
                        _invitedUsers.value = it.groupSendingInviteMembers
                        _invitedUsersHasNextPage.value = it.hasNext
                    }.onFail {
                        managementGroupMemberEvent(ManagementGroupMemberViewModel.ManagementGroupMemberEvent.ShowToastMessage("초대한 유저 불러오기 실패"))
                        managementGroupMemberEvent(ManagementGroupMemberViewModel.ManagementGroupMemberEvent.SwipeRefreshLayoutDismissLoading)
                    }
                }
                managementGroupMemberEvent(ManagementGroupMemberViewModel.ManagementGroupMemberEvent.SwipeRefreshLayoutDismissLoading)
            }
        }

    }
    override fun getLatestInvitedUserList(){
        getInvitedUserListJob?.cancel()
        getInvitedUserListJob = viewModelScope.launch {
            getInvitedUserListJob = viewModelScope.launch {
                getGroupInvitedUsersUseCase(
                    groupId = groupId.value,
                    pagingRequest = IdBasedPagingRequestDto(
                        size = 15,
                        pagingId = null
                    )
                ).collect{ result ->
                    result.onSuccess {
                        _invitedUsers.value = it.groupSendingInviteMembers
                        _invitedUsersHasNextPage.value = it.hasNext
                    }.onFail {
                        managementGroupMemberEvent(ManagementGroupMemberViewModel.ManagementGroupMemberEvent.ShowToastMessage("초대한 유저 불러오기 실패"))
                    }
                }
                managementGroupMemberEvent(ManagementGroupMemberViewModel.ManagementGroupMemberEvent.SwipeRefreshLayoutDismissLoading)
            }
        }
    }


}