package com.droidblossom.archive.presentation.ui.mypage.friend.addgroup

import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.domain.model.friend.Friend
import com.droidblossom.archive.domain.model.friend.FriendsSearchResponse
import com.droidblossom.archive.presentation.base.BaseViewModel
import com.droidblossom.archive.presentation.ui.mypage.friend.FriendViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddGroupViewModelImpl @Inject constructor(

) : BaseViewModel(), AddGroupViewModel {

    private val _addGroupEvent = MutableSharedFlow<AddGroupViewModel.AddGroupEvent>()
    override val addGroupEvent: SharedFlow<AddGroupViewModel.AddGroupEvent>
        get() = _addGroupEvent.asSharedFlow()

    private val _isCollapse = MutableStateFlow<Boolean>(false)
    override val isCollapse: MutableStateFlow<Boolean>
        get() = _isCollapse

    //friend
    private val _isFriendSearchOpen = MutableStateFlow(false)

    override val isFriendSearchOpen: StateFlow<Boolean>
        get() = _isFriendSearchOpen


    private val _friendList = MutableStateFlow<List<FriendsSearchResponse>>(listOf())
    override val friendList: StateFlow<List<FriendsSearchResponse>>
        get() = _friendList

    private val _checkedList  = MutableStateFlow<List<FriendsSearchResponse>>(listOf())
    override val checkedList: StateFlow<List<FriendsSearchResponse>>
        get() = _checkedList

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

    override fun search() {}

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


    override fun getFriendList() {
        TODO("Not yet implemented")
    }
}