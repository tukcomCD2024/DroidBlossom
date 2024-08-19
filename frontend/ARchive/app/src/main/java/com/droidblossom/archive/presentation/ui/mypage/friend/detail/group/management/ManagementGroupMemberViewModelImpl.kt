package com.droidblossom.archive.presentation.ui.mypage.friend.detail.group.management

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.data.dto.common.IdBasedPagingRequestDto
import com.droidblossom.archive.data.dto.common.PagingRequestDto
import com.droidblossom.archive.data.dto.group.request.InviteGroupRequestDto
import com.droidblossom.archive.domain.model.group.GroupInvitedUser
import com.droidblossom.archive.domain.model.group.GroupMember
import com.droidblossom.archive.domain.usecase.friend.FriendForGroupInvitePageUseCase
import com.droidblossom.archive.domain.usecase.group.CancelGroupInviteUseCase
import com.droidblossom.archive.domain.usecase.group.DeleteGroupMemberUseCase
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
    private val getGroupInvitedUsersUseCase: GetGroupInvitedUsersUseCase,
    private val cancelGroupInviteUseCase: CancelGroupInviteUseCase,
    private val deleteGroupMemberUseCase: DeleteGroupMemberUseCase
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
                    managementGroupMemberEvent(ManagementGroupMemberViewModel.ManagementGroupMemberEvent.ShowToastMessage("그룹원 목록을 불러오는데 실패했습니다. 잠시 후 다시 시도해주세요."))
                }
            }
        }
    }

    override fun inviteFriendsToGroup(){
        viewModelScope.launch {
            val targetIds = groupInviteeList.value.map { it.id }
            groupInviteUseCase(
                InviteGroupRequestDto(
                    groupId.value,
                    targetIds
                )
            ).collect{ result ->
                result.onSuccess {
                    load()
                    managementGroupMemberEvent(ManagementGroupMemberViewModel.ManagementGroupMemberEvent.ShowToastMessage("그룹 초대를 성공했습니다."))
                }.onFail {
                    load()
                    managementGroupMemberEvent(ManagementGroupMemberViewModel.ManagementGroupMemberEvent.ShowToastMessage("그룹 초대를 실패했습니다. 잠시 후 다시 시도해주세요."))
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
                        managementGroupMemberEvent(ManagementGroupMemberViewModel.ManagementGroupMemberEvent.ShowToastMessage("친구 목록을 불러오는데 실패했습니다. 잠시 후 다시 시도해주세요."))
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
                    managementGroupMemberEvent(ManagementGroupMemberViewModel.ManagementGroupMemberEvent.ShowToastMessage("친구 목록을 불러오는데 실패했습니다. 잠시 후 다시 시도해주세요."))
                }
            }
            managementGroupMemberEvent(ManagementGroupMemberViewModel.ManagementGroupMemberEvent.SwipeRefreshLayoutDismissLoading)
        }
    }

    override fun onClickInvitableFriend(position: Int){
        if (invitableFriends.value[position].isChecked){
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
            getInvitedUserListJob?.cancel()
            getInvitedUserListJob = viewModelScope.launch {
                getGroupInvitedUsersUseCase(
                    groupId = groupId.value,
                    pagingRequest = IdBasedPagingRequestDto(
                        size = 15,
                        pagingId = invitedUsers.value.lastOrNull()?.groupInviteId
                    )
                ).collect{ result ->
                    result.onSuccess {
                        _invitedUsers.value += it.groupSendingInviteMembers
                        _invitedUsersHasNextPage.value = it.hasNext
                    }.onFail {
                        managementGroupMemberEvent(ManagementGroupMemberViewModel.ManagementGroupMemberEvent.ShowToastMessage("초대 보낸 유저 목록을 불러오는데 실패했습니다. 잠시 후 다시 시도해주세요."))
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
                    managementGroupMemberEvent(ManagementGroupMemberViewModel.ManagementGroupMemberEvent.ShowToastMessage("초대 보낸 유저 목록을 불러오는데 실패했습니다. 잠시 후 다시 시도해주세요."))
                    managementGroupMemberEvent(ManagementGroupMemberViewModel.ManagementGroupMemberEvent.ShowToastMessage("초대한 유저 불러오기 실패"))
                }
            }
            managementGroupMemberEvent(ManagementGroupMemberViewModel.ManagementGroupMemberEvent.SwipeRefreshLayoutDismissLoading)
        }
    }

    override fun onClickInvitedUser(position: Int){
        viewModelScope.launch {
            val user = invitedUsers.value[position]
            cancelGroupInviteUseCase(groupInviteId = user.groupInviteId).collect{ result ->
                result.onSuccess {
                    _invitedUsers.value -= user
                    managementGroupMemberEvent(ManagementGroupMemberViewModel.ManagementGroupMemberEvent.ShowToastMessage("그룹 초대를 취소했습니다."))
                }.onFail {
                    if (it == 404){
                        _invitedUsers.value -= user
                        managementGroupMemberEvent(ManagementGroupMemberViewModel.ManagementGroupMemberEvent.ShowToastMessage("이미 처리된 요청입니다."))
                    }else{
                        managementGroupMemberEvent(ManagementGroupMemberViewModel.ManagementGroupMemberEvent.ShowToastMessage("그룹 초대 취소를 실패했습니다. 잠시 후 다시 시도해주세요."))
                    }
                }

            }
        }
    }

    override fun kickGroupMember(groupMember: GroupMember) {
        viewModelScope.launch {
            deleteGroupMemberUseCase(
                groupId = groupId.value,
                groupMemberId = groupMember.memberId
            ).collect{ result ->
                result.onSuccess {
                    _groupMembers.value -= groupMember
                    managementGroupMemberEvent(ManagementGroupMemberViewModel.ManagementGroupMemberEvent.ShowToastMessage("그룹원 추방을 성공했습니다."))

                }.onFail {
                    if (it == 404){
                        _groupMembers.value -= groupMember
                        managementGroupMemberEvent(ManagementGroupMemberViewModel.ManagementGroupMemberEvent.ShowToastMessage("이 그룹원은 이미 그룹에 속해 있지 않습니다."))
                    }else{
                        managementGroupMemberEvent(ManagementGroupMemberViewModel.ManagementGroupMemberEvent.ShowToastMessage("그룹원 추방을 실패했습니다. 잠시 후 다시 시도해주세요."))
                    }
                }
            }
        }
    }

}