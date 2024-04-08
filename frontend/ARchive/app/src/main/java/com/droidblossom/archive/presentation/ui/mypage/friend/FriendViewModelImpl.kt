package com.droidblossom.archive.presentation.ui.mypage.friend

import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.domain.model.friend.Friend
import com.droidblossom.archive.domain.usecase.friend.FriendDeleteUseCase
import com.droidblossom.archive.domain.usecase.friend.FriendsPageUseCase
import com.droidblossom.archive.presentation.base.BaseViewModel
import com.droidblossom.archive.util.DateUtils
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendViewModelImpl @Inject constructor(
    private val friendsPageUseCase: FriendsPageUseCase,
    private val friendDeleteUseCase: FriendDeleteUseCase
) : BaseViewModel(), FriendViewModel {


    private val _isFriendSearchOpen = MutableStateFlow(false)
    override val isFriendSearchOpen: StateFlow<Boolean>
        get() = _isFriendSearchOpen


    private val _friendListUI = MutableStateFlow<List<Friend>>(listOf())
    override val friendListUI: StateFlow<List<Friend>>
        get() = _friendListUI

    private val _friendList = MutableStateFlow<List<Friend>>(listOf())
    override val friendList: StateFlow<List<Friend>>
        get() = _friendList

    private val friendHasNextPage = MutableStateFlow(true)

    private val friendLastCreatedTime = MutableStateFlow(DateUtils.dataServerString)

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

    override fun searchFriend(){

    }

    override fun getFriendList() {
        viewModelScope.launch {
            if (friendHasNextPage.value) {
                friendsPageUseCase(15, friendLastCreatedTime.value).collect { result ->
                    result.onSuccess {
                        friendHasNextPage.value = it.hasNext
                        _friendListUI.emit(_friendListUI.value + it.friends)
                        _friendList.emit(friendList.value + it.friends)
                        friendLastCreatedTime.value = it.friends.last().createdAt
                    }.onFail {
//                        .emit(
//                            NotificationViewModel.NotificationEvent.ShowToastMessage(
//                                "알림 불러오기 실패"
//                            )
//                        )
                    }
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
}