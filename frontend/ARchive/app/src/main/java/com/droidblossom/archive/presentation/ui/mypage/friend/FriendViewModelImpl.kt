package com.droidblossom.archive.presentation.ui.mypage.friend

import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.data.dto.common.PagingRequestDto
import com.droidblossom.archive.domain.model.friend.Friend
import com.droidblossom.archive.domain.model.group.GroupSummary
import com.droidblossom.archive.domain.usecase.friend.FriendDeleteUseCase
import com.droidblossom.archive.domain.usecase.friend.FriendsPageUseCase
import com.droidblossom.archive.domain.usecase.group.GetGroupPageUseCase
import com.droidblossom.archive.presentation.base.BaseViewModel
import com.droidblossom.archive.presentation.base.BaseViewModel.Companion.throttleFirst
import com.droidblossom.archive.presentation.ui.home.notification.NotificationViewModel
import com.droidblossom.archive.presentation.ui.mypage.friend.addfriend.AddFriendViewModel
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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class FriendViewModelImpl @Inject constructor(
    private val friendsPageUseCase: FriendsPageUseCase,
    private val friendDeleteUseCase: FriendDeleteUseCase,
    private val getGroupPageUseCase: GetGroupPageUseCase,
) : BaseViewModel(), FriendViewModel {

    private val _friendEvent = MutableSharedFlow<FriendViewModel.FriendEvent>()
    override val friendEvent: SharedFlow<FriendViewModel.FriendEvent>
        get() = _friendEvent.asSharedFlow()

    //friend
    private val _isFriendSearchOpen = MutableStateFlow(false)

    override val isFriendSearchOpen: StateFlow<Boolean>
        get() = _isFriendSearchOpen
    override val searchFriendText: MutableStateFlow<String> = MutableStateFlow("")


    private val _friendListUI = MutableStateFlow<List<Friend>>(listOf())
    override val friendListUI: StateFlow<List<Friend>>
        get() = _friendListUI

    private val _friendList = MutableStateFlow<List<Friend>>(listOf())
    override val friendList: StateFlow<List<Friend>>
        get() = _friendList

    private val friendHasNextPage = MutableStateFlow(true)

    private val friendLastCreatedTime = MutableStateFlow(DateUtils.dataServerString)

    //group
    private val _isGroupSearchOpen = MutableStateFlow(false)

    override val isGroupSearchOpen: StateFlow<Boolean>
        get() = _isGroupSearchOpen

    private val _groupListUi = MutableStateFlow<List<GroupSummary>>(listOf())
    override val groupListUI: StateFlow<List<GroupSummary>>
        get() = _groupListUi

    override fun setEvent(event: FriendViewModel.FriendEvent) {
        viewModelScope.launch {
            _friendEvent.emit(event)
        }
    }

    private val groupHasNextPage = MutableStateFlow(true)

    private val groupLastCreatedTime = MutableStateFlow(DateUtils.dataServerString)


    private val scrollFriendEventChannel = Channel<Unit>(Channel.CONFLATED)
    private val scrollFriendEventFlow =
        scrollFriendEventChannel.receiveAsFlow().throttleFirst(1000, TimeUnit.MILLISECONDS)

    private var getFriendListJob: Job? = null

    private val scrollGroupEventChannel = Channel<Unit>(Channel.CONFLATED)
    private val scrollGroupEventFlow =
        scrollGroupEventChannel.receiveAsFlow().throttleFirst(1000, TimeUnit.MILLISECONDS)

    private var getGroupListJob: Job? = null

    init {
        viewModelScope.launch {
            scrollFriendEventFlow.collect {
                getFriendList()
            }

        }
        viewModelScope.launch {
            scrollGroupEventFlow.collect {
                getGroupList()
            }
        }
    }

    override fun onScrollNearBottomFriend() {
        scrollFriendEventChannel.trySend(Unit)
    }

    override fun onScrollNearBottomGroup() {
        scrollGroupEventChannel.trySend(Unit)
    }

    //friend
    override fun openSearchFriend() {
        viewModelScope.launch {
            _isFriendSearchOpen.emit(true)
        }
    }

    override fun closeSearchFriend() {
        viewModelScope.launch {
            _isFriendSearchOpen.emit(false)
        }
    }

    override fun searchFriend() {
        viewModelScope.launch {
            if (searchFriendText.value.isBlank()) {
                _friendListUI.emit(friendList.value)
            } else {
                _friendListUI.emit(friendList.value.filter { it.nickname.contains(searchFriendText.value) })
            }
        }
    }

    override fun getFriendList() {
        if (friendHasNextPage.value) {
            getFriendListJob?.cancel()
            getFriendListJob = viewModelScope.launch {
                friendsPageUseCase(
                    PagingRequestDto(
                        15,
                        friendLastCreatedTime.value
                    )
                ).collect { result ->
                    result.onSuccess {
                        friendHasNextPage.value = it.hasNext
                        if (friendListUI.value.isEmpty()) {
                            _friendListUI.emit(it.friends)
                            _friendList.emit(it.friends)
                        } else {
                            _friendListUI.emit(_friendListUI.value + it.friends)
                            _friendList.emit(friendList.value + it.friends)
                        }
                        friendLastCreatedTime.value = it.friends.last().createdAt
                    }.onFail {
                        _friendEvent.emit(
                            FriendViewModel.FriendEvent.ShowToastMessage(
                                "친구 리스트 불러오기 실패. 잠시후 시도해 주세요"
                            )
                        )
                    }
                }
            }
        }
    }

    override fun getGroupList() {
        if (groupHasNextPage.value) {
            getGroupListJob?.cancel()
            getGroupListJob = viewModelScope.launch {
                getGroupPageUseCase(
                    PagingRequestDto(
                        100,
                        groupLastCreatedTime.value
                    )
                ).collect { result ->
                    result.onSuccess {
                        groupHasNextPage.value = it.hasNext
                        if (friendListUI.value.isEmpty()) {
                            _groupListUi.emit(it.groups)
                        } else {
                            _groupListUi.emit(_groupListUi.value + it.groups)
                        }
                        groupLastCreatedTime.value = it.groups.last().createdAt
                    }.onFail {
                        _friendEvent.emit(
                            FriendViewModel.FriendEvent.ShowToastMessage(
                                "그룹 리스트 불러오기 실패. 잠시후 시도해 주세요"
                            )
                        )
                    }
                }
            }
        }
    }

    override fun getGroupLastList() {
        getGroupListJob?.cancel()
        getGroupListJob = viewModelScope.launch {
            getGroupPageUseCase(
                PagingRequestDto(
                    100,
                    DateUtils.dataServerString
                )
            ).collect { result ->
                result.onSuccess {
                    groupHasNextPage.value = it.hasNext
                    _groupListUi.emit(it.groups)
                    friendLastCreatedTime.value = it.groups.last().createdAt
                    _friendEvent.emit(FriendViewModel.FriendEvent.OnRefreshEnd)
                }.onFail {
                    _friendEvent.emit(
                        FriendViewModel.FriendEvent.ShowToastMessage(
                            "그룹 리스트 불러오기 실패. 잠시후 시도해 주세요"
                        )
                    )
                }
            }
        }
    }

    override fun changeDeleteOpen(previousPosition: Int?, currentPosition: Int) {
        viewModelScope.launch {
            val newList = _friendListUI.value
            previousPosition?.let {
                newList[it].isOpenDelete = false
            }
            newList[currentPosition].isOpenDelete = true
            _friendListUI.emit(newList)
        }
    }

    override fun deleteFriend(friend: Friend) {
        viewModelScope.launch {
            friendDeleteUseCase(friend.id).collect { result ->
                result.onSuccess {
                    val list = friendListUI.value.toMutableList()
                    list.remove(friend)
                    _friendListUI.emit(list)
                }.onFail {
                    if (it == 400){
                        val list = friendListUI.value.toMutableList()
                        list.remove(friend)
                        _friendListUI.emit(list)
                    }else{
                        _friendEvent.emit(
                            FriendViewModel.FriendEvent.ShowToastMessage(
                                "친구 삭제 실패. 잠시후 시도해 주세요"
                            )
                        )
                    }
                }
            }
        }
    }

    //group
    override fun openSearchGroup() {
        viewModelScope.launch {
            _isGroupSearchOpen.emit(true)
        }
    }

    override fun closeSearchGroup() {
        viewModelScope.launch {
            _isGroupSearchOpen.emit(false)
        }
    }

    override fun searchGroup() {

    }

    override fun removeGroup(groupId: Long) {
        val groupToRemove = _groupListUi.value.find { it.id == groupId }
        groupToRemove?.let {
            _groupListUi.value -= it
        }
    }
}