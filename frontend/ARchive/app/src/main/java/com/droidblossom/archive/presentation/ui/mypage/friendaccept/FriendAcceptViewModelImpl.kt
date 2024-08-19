package com.droidblossom.archive.presentation.ui.mypage.friendaccept

import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.ARchiveApplication
import com.droidblossom.archive.R
import com.droidblossom.archive.data.dto.common.PagingRequestDto
import com.droidblossom.archive.domain.model.friend.Friend
import com.droidblossom.archive.domain.model.friend.FriendAcceptRequest
import com.droidblossom.archive.domain.model.group.GroupInviteSummary
import com.droidblossom.archive.domain.usecase.friend.FriendDeleteSendUseCase
import com.droidblossom.archive.domain.usecase.friend.FriendDenyRequestUseCase
import com.droidblossom.archive.domain.usecase.friend.FriendsAcceptRequestUseCase
import com.droidblossom.archive.domain.usecase.friend.FriendsRequestsPageUseCase
import com.droidblossom.archive.domain.usecase.friend.FriendsSendRequestsPageUseCase
import com.droidblossom.archive.domain.usecase.group.AcceptInviteGroupUseCase
import com.droidblossom.archive.domain.usecase.group.DenyInviteGroupUseCase
import com.droidblossom.archive.domain.usecase.group.GetGroupInvitePageUseCase
import com.droidblossom.archive.presentation.base.BaseViewModel
import com.droidblossom.archive.presentation.ui.mypage.friend.FriendViewModel
import com.droidblossom.archive.presentation.ui.social.page.friend.SocialFriendViewModel
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
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class FriendAcceptViewModelImpl @Inject constructor(
    private val friendsRequestsPageUseCase: FriendsRequestsPageUseCase,
    private val friendDenyRequestUseCase: FriendDenyRequestUseCase,
    private val friendAcceptRequestUseCase: FriendsAcceptRequestUseCase,
    private val groupsRequestsPageUseCase: GetGroupInvitePageUseCase,
    private val groupAcceptRequestUseCase: AcceptInviteGroupUseCase,
    private val groupDenyRequestUseCase: DenyInviteGroupUseCase,
    private val friendsSendRequestsPageUseCase: FriendsSendRequestsPageUseCase,
    private val friendDeleteSendUseCase: FriendDeleteSendUseCase,
) : BaseViewModel(), FriendAcceptViewModel {

    private val _friendAcceptEvent = MutableSharedFlow<FriendAcceptViewModel.FriendAcceptEvent>()
    override val friendAcceptEvent: SharedFlow<FriendAcceptViewModel.FriendAcceptEvent>
        get() = _friendAcceptEvent.asSharedFlow()

    private val _friendAcceptList = MutableStateFlow<List<Friend>>(listOf())
    override val friendAcceptList: StateFlow<List<Friend>>
        get() = _friendAcceptList
    private val _friendSendAcceptList = MutableStateFlow<List<Friend>>(listOf())

    override val friendSendAcceptList: StateFlow<List<Friend>>
        get() = _friendSendAcceptList

    private val _groupAcceptList = MutableStateFlow<List<GroupInviteSummary>>(listOf())
    override val groupAcceptList: StateFlow<List<GroupInviteSummary>>
        get() = _groupAcceptList

    private val friendHasNextPage = MutableStateFlow(true)
    private val friendLastCreatedTime = MutableStateFlow(DateUtils.dataServerString)

    private val scrollFriendEventChannel = Channel<Unit>(Channel.CONFLATED)
    private val scrollFriendEventFlow =
        scrollFriendEventChannel.receiveAsFlow().throttleFirst(1000, TimeUnit.MILLISECONDS)

    private var getFriendAcceptRequestListJob: Job? = null

    private val groupHasNextPage = MutableStateFlow(true)
    private val groupLastCreatedTime = MutableStateFlow(DateUtils.dataServerString)

    private val scrollGroupEventChannel = Channel<Unit>(Channel.CONFLATED)
    private val scrollGroupEventFlow =
        scrollGroupEventChannel.receiveAsFlow().throttleFirst(1000, TimeUnit.MILLISECONDS)

    private var getGroupAcceptRequestListJob: Job? = null

    private val friendSendHasNextPage = MutableStateFlow(true)
    private val friendSendLastCreatedTime = MutableStateFlow(DateUtils.dataServerString)

    private val scrollFriendSendEventChannel = Channel<Unit>(Channel.CONFLATED)
    private val scrollFriendSendEventFlow =
        scrollFriendSendEventChannel.receiveAsFlow().throttleFirst(1000, TimeUnit.MILLISECONDS)

    private var getFriendSendAcceptRequestListJob: Job? = null


    init {
        viewModelScope.launch {
            scrollFriendEventFlow.collect {
                if (friendAcceptList.value.isEmpty()){
                    getLastedFriendAcceptList()
                }else{
                    getFriendAcceptListPage()
                }
            }
        }
        viewModelScope.launch {
            scrollGroupEventFlow.collect {
                if (groupAcceptList.value.isEmpty()){
                    getLastedGroupAcceptList()
                }else{
                    getGroupAcceptListPage()
                }
            }
        }
        viewModelScope.launch {
            scrollFriendSendEventFlow.collect{
                getFriendSendAcceptListPage()
            }
        }
    }

    override fun onScrollFriendNearBottom() {
        scrollFriendEventChannel.trySend(Unit)
    }

    override fun getFriendAcceptListPage() {
        if (friendHasNextPage.value){
            getFriendAcceptRequestListJob?.cancel()
            getFriendAcceptRequestListJob = viewModelScope.launch {
                friendsRequestsPageUseCase(
                    PagingRequestDto(
                        15,
                        friendLastCreatedTime.value
                    )
                ).collect { result ->
                    result.onSuccess {
                        friendHasNextPage.value = it.hasNext
                        _friendAcceptList.emit(friendAcceptList.value + it.friends)
                        if (friendAcceptList.value.isNotEmpty()) {
                            friendLastCreatedTime.value = it.friends.last().createdAt
                        }
                    }.onFail {
                        _friendAcceptEvent.emit(
                            FriendAcceptViewModel.FriendAcceptEvent.ShowToastMessage(
                                "친구 목록을 불러오는데 실패했습니다. "+ ARchiveApplication.getString(
                                    R.string.reTryMessage
                                )
                            )
                        )
                    }
                }
            }
        }
    }

    override fun getLastedFriendAcceptList() {
        getFriendAcceptRequestListJob?.cancel()
        getFriendAcceptRequestListJob = viewModelScope.launch {
            friendsRequestsPageUseCase(
                PagingRequestDto(
                    15,
                    DateUtils.dataServerString,
                )
            ).collect { result ->
                result.onSuccess {
                    friendHasNextPage.value = it.hasNext
                    _friendAcceptList.emit(it.friends)
                    if (friendAcceptList.value.isNotEmpty()) {
                        friendLastCreatedTime.value = it.friends.last().createdAt
                    }

                }.onFail {
                    _friendAcceptEvent.emit(
                        FriendAcceptViewModel.FriendAcceptEvent.ShowToastMessage(
                            "친구 목록을 불러오는데 실패했습니다. "+ ARchiveApplication.getString(
                                R.string.reTryMessage
                            )
                        )
                    )

                }
            }
            _friendAcceptEvent.emit(FriendAcceptViewModel.FriendAcceptEvent.SwipeRefreshLayoutDismissLoading)
        }
    }

    override fun denyFriendRequest(friend: Friend) {
        viewModelScope.launch {
            friendDenyRequestUseCase(friend.id).collect { result ->
                result.onSuccess {
                    removeFriendItem(friend)
                }.onFail {
                    if (it == 404){
                        removeFriendItem(friend)
                        _friendAcceptEvent.emit(
                            FriendAcceptViewModel.FriendAcceptEvent.ShowToastMessage(
                                "이미 처리된 요청입니다."
                            )
                        )
                    }else{
                        _friendAcceptEvent.emit(
                            FriendAcceptViewModel.FriendAcceptEvent.ShowToastMessage(
                                "요청을 실패했습니다. "+ ARchiveApplication.getString(
                                    R.string.reTryMessage
                                )
                            )
                        )
                    }
                }

            }
        }
    }

    override fun acceptFriendRequest(friend: Friend) {
        viewModelScope.launch {
            friendAcceptRequestUseCase(FriendAcceptRequest(friend.id)).collect { result ->
                result.onSuccess {
                    removeFriendItem(friend)
                }.onFail {
                    if (it == 404){
                        removeFriendItem(friend)
                        _friendAcceptEvent.emit(
                            FriendAcceptViewModel.FriendAcceptEvent.ShowToastMessage(
                                "이미 처리된 요청입니다."
                            )
                        )
                    }else if (it == 500){
                        removeFriendItem(friend)
                    }
                    else{
                        _friendAcceptEvent.emit(
                            FriendAcceptViewModel.FriendAcceptEvent.ShowToastMessage(
                                "요청을 실패했습니다. "+ ARchiveApplication.getString(
                                    R.string.reTryMessage
                                )
                            )
                        )
                    }
                }
            }
        }
    }

    override fun getGroupAcceptListPage() {
        getGroupAcceptRequestListJob?.cancel()
        getGroupAcceptRequestListJob = viewModelScope.launch {
            if (groupHasNextPage.value) {
                groupsRequestsPageUseCase(
                    PagingRequestDto(
                        15,
                        groupLastCreatedTime.value
                    )
                ).collect { result ->
                    result.onSuccess {
                        groupHasNextPage.value = it.hasNext
                        _groupAcceptList.emit(groupAcceptList.value + it.groups)
                        if (groupAcceptList.value.isNotEmpty()) {
                            groupLastCreatedTime.value = it.groups.last().createdAt
                        }

                    }.onFail {
                        _friendAcceptEvent.emit(
                            FriendAcceptViewModel.FriendAcceptEvent.ShowToastMessage(
                                "요청 목록을 불러오는데 실패했습니다. "+ ARchiveApplication.getString(
                                    R.string.reTryMessage
                                )
                            )
                        )
                    }
                }
            }
        }
    }

    override fun getLastedGroupAcceptList() {
        getGroupAcceptRequestListJob?.cancel()
        getGroupAcceptRequestListJob = viewModelScope.launch {
            groupsRequestsPageUseCase(
                PagingRequestDto(
                    15,
                    DateUtils.dataServerString
                )
            ).collect { result ->
                result.onSuccess {
                    groupHasNextPage.value = it.hasNext
                    _groupAcceptList.emit(it.groups)
                    if (groupAcceptList.value.isNotEmpty()) {
                        groupLastCreatedTime.value = it.groups.last().createdAt
                    }

                }.onFail {
                    _friendAcceptEvent.emit(
                        FriendAcceptViewModel.FriendAcceptEvent.ShowToastMessage(
                            "요청 목록을 불러오는데 실패했습니다. "+ ARchiveApplication.getString(
                                R.string.reTryMessage
                            )
                        )
                    )
                }
            }
            _friendAcceptEvent.emit(FriendAcceptViewModel.FriendAcceptEvent.SwipeRefreshLayoutDismissLoading)
        }
    }

    override fun onScrollGroupNearBottom() {
        scrollGroupEventChannel.trySend(Unit)
    }

    override fun denyGroupRequest(group: GroupInviteSummary) {
        viewModelScope.launch {
            groupDenyRequestUseCase(group.groupId, group.groupId).collect { result ->
                result.onSuccess {
                    removeGroupItem(group)
                }.onFail {
                    if (it == 404){
                        removeGroupItem(group)
                        _friendAcceptEvent.emit(
                            FriendAcceptViewModel.FriendAcceptEvent.ShowToastMessage(
                                "이미 처리된 요청입니다."
                            )
                        )
                    }else {
                        _friendAcceptEvent.emit(
                            FriendAcceptViewModel.FriendAcceptEvent.ShowToastMessage(
                                "요청을 실패했습니다. "+ ARchiveApplication.getString(
                                    R.string.reTryMessage
                                )
                            )
                        )
                    }
                }
            }
        }
    }

    override fun acceptGroupRequest(group: GroupInviteSummary) {
        viewModelScope.launch {
            groupAcceptRequestUseCase(group.groupId).collect { result ->
                result.onSuccess {
                    removeGroupItem(group)
                }.onFail {
                    if (it == 404){
                        removeGroupItem(group)
                        _friendAcceptEvent.emit(
                            FriendAcceptViewModel.FriendAcceptEvent.ShowToastMessage(
                                "이미 처리된 요청입니다."
                            )
                        )
                    }else{
                        _friendAcceptEvent.emit(
                            FriendAcceptViewModel.FriendAcceptEvent.ShowToastMessage(
                                "요청을 실패했습니다. "+ ARchiveApplication.getString(
                                    R.string.reTryMessage
                                )
                            )
                        )
                    }
                }

            }
        }
    }

    override fun getFriendSendAcceptListPage() {
        if (friendSendHasNextPage.value){
            getFriendSendAcceptRequestListJob?.cancel()
            getFriendSendAcceptRequestListJob = viewModelScope.launch {
                friendsSendRequestsPageUseCase(
                    PagingRequestDto(
                        15,
                        friendSendLastCreatedTime.value
                    )
                ).collect { result ->
                    result.onSuccess {
                        friendSendHasNextPage.value = it.hasNext
                        _friendSendAcceptList.emit(friendSendAcceptList.value + it.friends)
                        if (friendSendAcceptList.value.isNotEmpty()) {
                            friendSendLastCreatedTime.value = it.friends.last().createdAt
                        }

                    }.onFail {
                        _friendAcceptEvent.emit(
                            FriendAcceptViewModel.FriendAcceptEvent.ShowToastMessage(
                                "요청 목록을 불러오는데 실패했습니다. "+ ARchiveApplication.getString(
                                    R.string.reTryMessage
                                )
                            )
                        )
                    }
                }
            }
        }

    }
    
    override fun onScrollFriendSendNearBottom() {
        scrollFriendSendEventChannel.trySend(Unit)
    }

    override fun deleteFriendSendRequest(friend: Friend) {
        viewModelScope.launch {
            friendDeleteSendUseCase(friend.id).collect { result ->
                result.onSuccess {
                    removeFriendSendItem(friend)
                }.onFail {
                    if (it == 404){
                        removeFriendSendItem(friend)
                        _friendAcceptEvent.emit(
                            FriendAcceptViewModel.FriendAcceptEvent.ShowToastMessage(
                                "이미 처리된 요청입니다."
                            )
                        )
                    }else{
                        _friendAcceptEvent.emit(
                            FriendAcceptViewModel.FriendAcceptEvent.ShowToastMessage(
                                "요청을 실패했습니다. "+ ARchiveApplication.getString(
                                    R.string.reTryMessage
                                )
                            )
                        )
                    }
                }

            }
        }
    }

    private fun removeFriendItem(friend: Friend) {
        viewModelScope.launch {
            _friendAcceptList.value -= friend
        }
    }

    private fun removeGroupItem(group: GroupInviteSummary) {
        viewModelScope.launch {
            _groupAcceptList.value -= group
        }
    }

    private fun removeFriendSendItem(friend: Friend) {
        viewModelScope.launch {
            _friendSendAcceptList.value -= friend
        }
    }

}