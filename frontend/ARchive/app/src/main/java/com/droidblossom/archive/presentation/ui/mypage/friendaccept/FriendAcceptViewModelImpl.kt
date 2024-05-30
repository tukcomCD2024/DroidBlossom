package com.droidblossom.archive.presentation.ui.mypage.friendaccept

import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.data.dto.common.PagingRequestDto
import com.droidblossom.archive.domain.model.friend.Friend
import com.droidblossom.archive.domain.model.friend.FriendAcceptRequest
import com.droidblossom.archive.domain.usecase.friend.FriendDenyRequestUseCase
import com.droidblossom.archive.domain.usecase.friend.FriendsAcceptRequestUseCase
import com.droidblossom.archive.domain.usecase.friend.FriendsRequestsPageUseCase
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
) : BaseViewModel(), FriendAcceptViewModel {

    private val _friendAcceptEvent = MutableSharedFlow<FriendAcceptViewModel.FriendAcceptEvent>()
    override val friendAcceptEvent: SharedFlow<FriendAcceptViewModel.FriendAcceptEvent>
        get() = _friendAcceptEvent.asSharedFlow()

    private val _friendAcceptList = MutableStateFlow<List<Friend>>(listOf())
    override val friendAcceptList: StateFlow<List<Friend>>
        get() = _friendAcceptList

    private val friendHasNextPage = MutableStateFlow(true)
    private val friendLastCreatedTime = MutableStateFlow(DateUtils.dataServerString)

    private val scrollFriendEventChannel = Channel<Unit>(Channel.CONFLATED)
    private val scrollFriendEventFlow =
        scrollFriendEventChannel.receiveAsFlow().throttleFirst(1000, TimeUnit.MILLISECONDS)

    private var getFriendAcceptRequestListJob: Job? = null

    init {
        viewModelScope.launch {
            scrollFriendEventFlow.collect {
                getFriendAcceptList()
            }
        }
    }

    override fun onScrollNearBottom() {
        scrollFriendEventChannel.trySend(Unit)
    }

    override fun getFriendAcceptList() {
        getFriendAcceptRequestListJob?.cancel()
        getFriendAcceptRequestListJob = viewModelScope.launch {
            if (friendHasNextPage.value) {
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
                                "친구 리스트 불러오기 실패. 잠시후 시도해 주세요"
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
                            "친구 리스트 불러오기 실패. 잠시후 시도해 주세요"
                        )
                    )

                }
            }
            _friendAcceptEvent.emit(FriendAcceptViewModel.FriendAcceptEvent.SwipeRefreshLayoutDismissLoading)
        }
    }

    override fun denyRequest(friend: Friend) {
        viewModelScope.launch {
            friendDenyRequestUseCase(friend.id).collect { result ->
                result.onSuccess {
                    removeItem(friend)
                }.onFail {
                    _friendAcceptEvent.emit(
                        FriendAcceptViewModel.FriendAcceptEvent.ShowToastMessage(
                            "요청 실패. 잠시후 시도해 주세요"
                        )
                    )
                }

            }
        }
    }

    override fun acceptRequest(friend: Friend) {
        viewModelScope.launch {
            friendAcceptRequestUseCase(FriendAcceptRequest(friend.id)).collect { result ->
                result.onSuccess {
                    removeItem(friend)
                }.onFail {
                    _friendAcceptEvent.emit(
                        FriendAcceptViewModel.FriendAcceptEvent.ShowToastMessage(
                            "요청 실패. 잠시후 시도해 주세요"
                        )
                    )
                }

            }
        }
    }

    fun removeItem(friend: Friend) {
        viewModelScope.launch {
            val newList = _friendAcceptList.value.toMutableList()
            newList.remove(friend)
            _friendAcceptList.emit(newList)
        }
    }

}