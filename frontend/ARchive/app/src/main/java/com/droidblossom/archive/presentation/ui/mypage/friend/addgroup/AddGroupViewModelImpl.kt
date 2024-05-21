package com.droidblossom.archive.presentation.ui.mypage.friend.addgroup

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.data.dto.common.PagingRequestDto
import com.droidblossom.archive.domain.model.friend.FriendsSearchResponse
import com.droidblossom.archive.domain.usecase.friend.FriendsForAddGroupPageUseCase
import com.droidblossom.archive.domain.usecase.group.GroupCreateUseCase
import com.droidblossom.archive.domain.usecase.s3.S3OneUrlGetUseCase
import com.droidblossom.archive.presentation.base.BaseViewModel
import com.droidblossom.archive.util.DateUtils
import com.droidblossom.archive.util.S3Util
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
class AddGroupViewModelImpl @Inject constructor(
    private val s3OneUrlGetUseCase: S3OneUrlGetUseCase,
    private val friendsForAddGroupPageUseCase: FriendsForAddGroupPageUseCase,
    private val groupCreateUseCase: GroupCreateUseCase,
    private val s3Util: S3Util,
) : BaseViewModel(), AddGroupViewModel {

    private val _addGroupEvent = MutableSharedFlow<AddGroupViewModel.AddGroupEvent>()
    override val addGroupEvent: SharedFlow<AddGroupViewModel.AddGroupEvent>
        get() = _addGroupEvent.asSharedFlow()
    override val groupTitle: MutableStateFlow<String> = MutableStateFlow("")
    override val groupContent: MutableStateFlow<String> = MutableStateFlow("")
    override val groupProfileUri: MutableStateFlow<Uri?> = MutableStateFlow(null)

    private val _isCollapse = MutableStateFlow<Boolean>(false)
    override val isCollapse: MutableStateFlow<Boolean>
        get() = _isCollapse

    private val _isFriendSearchOpen = MutableStateFlow(false)
    override val isFriendSearchOpen: StateFlow<Boolean>
        get() = _isFriendSearchOpen

    private val _friendListUI = MutableStateFlow<List<FriendsSearchResponse>>(listOf())
    override val friendListUI: StateFlow<List<FriendsSearchResponse>>
        get() = _friendListUI

    private val _checkedList = MutableStateFlow<List<FriendsSearchResponse>>(listOf())
    override val checkedList: StateFlow<List<FriendsSearchResponse>>
        get() = _checkedList

    private val friendHasNextPage = MutableStateFlow(true)

    private val friendLastCreatedTime = MutableStateFlow(DateUtils.dataServerString)


    private val scrollFriendEventChannel = Channel<Unit>(Channel.CONFLATED)
    private val scrollFriendEventFlow =
        scrollFriendEventChannel.receiveAsFlow().throttleFirst(1000, TimeUnit.MILLISECONDS)

    private var getFriendLstJob: Job? = null

    init {
        viewModelScope.launch {
            scrollFriendEventFlow.collect {
                getFriendList()
            }
        }
    }

    fun onScrollNearBottomFriend() {
        scrollFriendEventChannel.trySend(Unit)
    }

    override fun getFriendList() {
        if (friendHasNextPage.value) {
            getFriendLstJob?.cancel()
            getFriendLstJob = viewModelScope.launch {
                friendsForAddGroupPageUseCase(
                    PagingRequestDto(
                        15,
                        friendLastCreatedTime.value
                    )
                ).collect { result ->
                    result.onSuccess {
                        friendHasNextPage.value = it.hasNext
                        if (friendListUI.value.isEmpty()) {
                            _friendListUI.emit(it.friends)
                        } else {
                            _friendListUI.emit(_friendListUI.value + it.friends)
                        }
                        friendLastCreatedTime.value = it.friends.last().createdAt
                    }.onFail {
                        _addGroupEvent.emit(
                            AddGroupViewModel.AddGroupEvent.ShowToastMessage(
                                "친구 리스트 불러오기 실패. 잠시후 시도해 주세요"
                            )
                        )
                    }
                }
            }
        }
    }

    fun checkFriendList(position: Int) {
        viewModelScope.launch {
            val newAddList = friendListUI.value
            if (newAddList[position].isChecked) {
                newAddList[position].isChecked = false
                _checkedList.emit(newAddList.filter { it.isChecked })
            } else {
                newAddList[position].isChecked = true
                _checkedList.emit(newAddList.filter { it.isChecked })
            }
            _friendListUI.emit(newAddList)
        }
    }

    override fun expandedAppBar() {
        viewModelScope.launch {
            _isCollapse.emit(false)
        }
    }

    override fun collapsedAppBar() {
        viewModelScope.launch {
            _isCollapse.emit(true)
        }
    }

    override fun search() {

    }

    override fun openSearch() {
        viewModelScope.launch {
            _isFriendSearchOpen.emit(true)
        }
    }

    override fun closeSearch() {
        viewModelScope.launch {
            _isFriendSearchOpen.emit(true)
        }
    }

}