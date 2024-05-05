package com.droidblossom.archive.presentation.ui.mypage.friendaccept

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.domain.model.friend.Friend
import com.droidblossom.archive.domain.model.friend.FriendAcceptRequest
import com.droidblossom.archive.domain.usecase.friend.FriendDenyRequestUseCase
import com.droidblossom.archive.domain.usecase.friend.FriendsAcceptRequestUseCase
import com.droidblossom.archive.domain.usecase.friend.FriendsRequestsPageUseCase
import com.droidblossom.archive.presentation.base.BaseViewModel
import com.droidblossom.archive.presentation.ui.mypage.friend.FriendViewModel
import com.droidblossom.archive.util.DateUtils
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class FriendAcceptViewModelImpl @Inject constructor(
    private val friendsRequestsPageUseCase: FriendsRequestsPageUseCase,
    private val denyRequestUseCase: FriendDenyRequestUseCase,
    private val acceptRequestUseCase: FriendsAcceptRequestUseCase
) : BaseViewModel(), FriendAcceptViewModel {

    private val _friendAcceptEvent = MutableSharedFlow<FriendViewModel.FriendEvent>()
    override val friendAcceptEvent: SharedFlow<FriendViewModel.FriendEvent>
        get() = _friendAcceptEvent.asSharedFlow()

    private val _friendAcceptList = MutableStateFlow<List<Friend>>(listOf())
    override val friendAcceptList: StateFlow<List<Friend>>
        get() = _friendAcceptList

    private val friendHasNextPage = MutableStateFlow(true)
    private val friendLastCreatedTime = MutableStateFlow(DateUtils.dataServerString)

    private val scrollEventChannel = Channel<Unit>(Channel.CONFLATED)
    private val scrollEventFlow = scrollEventChannel.receiveAsFlow().throttleFirst(1000, TimeUnit.MILLISECONDS)

    private var getAcceptRequestListJob: Job? = null
    init {
        viewModelScope.launch {
            scrollEventFlow.collect {
                getFriendAcceptList()
            }
        }
    }

    override fun onScrollNearBottom() {
        scrollEventChannel.trySend(Unit)
    }

    override fun getFriendAcceptList() {
        getAcceptRequestListJob?.cancel()
        getAcceptRequestListJob = viewModelScope.launch {
            if (friendHasNextPage.value) {
                friendsRequestsPageUseCase(15, friendLastCreatedTime.value).collect { result ->
                    result.onSuccess {
                        friendHasNextPage.value = it.hasNext
                        _friendAcceptList.emit(friendAcceptList.value + it.friends)
                        if (friendAcceptList.value.isNotEmpty()) {
                            friendLastCreatedTime.value = it.friends.last().createdAt
                        }

                    }.onFail {
                        _friendAcceptEvent.emit(
                            FriendViewModel.FriendEvent.ShowToastMessage(
                                "친구 리스트 불러오기 실패. 잠시후 시도해 주세요"
                            )
                        )
                    }
                }
            }
        }
    }

    override fun denyRequest(friend: Friend) {
        viewModelScope.launch {
            denyRequestUseCase(friend.id).collect { result ->
                result.onSuccess {
                    removeItem(friend)
                }.onFail {
                    _friendAcceptEvent.emit(
                        FriendViewModel.FriendEvent.ShowToastMessage(
                            "요청 실패. 잠시후 시도해 주세요"
                        )
                    )
                }

            }
        }
    }

    override fun acceptRequest(friend: Friend) {
        viewModelScope.launch {
            acceptRequestUseCase(FriendAcceptRequest(friend.id)).collect { result ->
                result.onSuccess {
                    removeItem(friend)
                }.onFail {
                    _friendAcceptEvent.emit(
                        FriendViewModel.FriendEvent.ShowToastMessage(
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