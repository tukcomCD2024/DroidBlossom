package com.droidblossom.archive.presentation.ui.mypage.friend.addfriend

import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.ARchiveApplication
import com.droidblossom.archive.R
import com.droidblossom.archive.data.dto.friend.request.FriendsReqRequestDto
import com.droidblossom.archive.data.dto.friend.request.PhoneBooks
import com.droidblossom.archive.domain.model.friend.FriendReqRequest
import com.droidblossom.archive.domain.model.friend.FriendsSearchPhoneRequest
import com.droidblossom.archive.domain.model.friend.FriendsSearchRequest
import com.droidblossom.archive.domain.model.friend.FriendsSearchResponse
import com.droidblossom.archive.domain.usecase.friend.FriendListRequestUseCase
import com.droidblossom.archive.domain.usecase.friend.FriendsRequestUseCase
import com.droidblossom.archive.domain.usecase.friend.FriendsSearchPhoneUseCase
import com.droidblossom.archive.domain.usecase.friend.FriendsSearchUseCase
import com.droidblossom.archive.presentation.base.BaseViewModel
import com.droidblossom.archive.presentation.model.mypage.friend.AddTagSearchFriendUIModel
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddFriendViewModelImpl @Inject constructor(
    private val friendsSearchUseCase: FriendsSearchUseCase,
    private val friendsRequestUseCase: FriendsRequestUseCase,
    private val friendListRequestUseCase: FriendListRequestUseCase,
    private val friendsSearchPhoneUseCase: FriendsSearchPhoneUseCase
) : BaseViewModel(), AddFriendViewModel {

    private val _addEvent = MutableSharedFlow<AddFriendViewModel.AddEvent>()

    override val addEvent: SharedFlow<AddFriendViewModel.AddEvent>
        get() = _addEvent.asSharedFlow()

    //name

    private val _addFriendListUI = MutableStateFlow<List<AddTagSearchFriendUIModel>>(listOf())
    override val addFriendListUI: StateFlow<List<AddTagSearchFriendUIModel>>
        get() = _addFriendListUI

    private val _addTagSearchFriendListUI = MutableStateFlow<List<AddTagSearchFriendUIModel>>(listOf())
    override val addTagSearchFriendListUI: StateFlow<List<AddTagSearchFriendUIModel>>
        get() = _addTagSearchFriendListUI


    private val _addFriendList = MutableStateFlow<List<AddTagSearchFriendUIModel>>(listOf())
    override val addFriendList: StateFlow<List<AddTagSearchFriendUIModel>>
        get() = _addFriendList


    override val searchFriendText: MutableStateFlow<String> = MutableStateFlow("")
    private val _checkedList = MutableStateFlow<List<AddTagSearchFriendUIModel>>(listOf())
    override val checkedList: StateFlow<List<AddTagSearchFriendUIModel>>
        get() = _checkedList

    override val tagT: MutableStateFlow<String> = MutableStateFlow("")


    //Num

    private val _isSearchNumOpen = MutableStateFlow(false)
    override val isSearchNumOpen: StateFlow<Boolean>
        get() = _isSearchNumOpen


    //Name
    override fun requestFriend(friendId: Long) {
        viewModelScope.launch {
            friendsRequestUseCase(FriendReqRequest(friendId = friendId)).collect { result ->
                result.onSuccess {
                    _addEvent.emit(AddFriendViewModel.AddEvent.ShowToastMessage("친구 요청을 보냈습니다."))
                    changeRequestUI(friendId)
                }.onFail {
                    if (it == 500) {
                        _addEvent.emit(AddFriendViewModel.AddEvent.ShowToastMessage("친구 요청을 보냈습니다."))
                        changeRequestUI(friendId)
                    } else {
                        _addEvent.emit(
                            AddFriendViewModel.AddEvent.ShowToastMessage(
                                "친구 요청을 실패했습니다. " + ARchiveApplication.getString(
                                    R.string.reTryMessage
                                )
                            )
                        )
                    }
                }
            }
        }


    }

    override fun requestFriendList(){
        viewModelScope.launch {
            val ids = checkedList.value.map { it.id }
            friendListRequestUseCase(FriendsReqRequestDto(ids)).collect{ result ->
                result.onSuccess {
                    _addEvent.emit(AddFriendViewModel.AddEvent.ShowToastMessage("친구 요청을 보냈습니다."))
                    changeRequestsUI(ids)
                }.onFail {
                    if (it == 500) {
                        _addEvent.emit(AddFriendViewModel.AddEvent.ShowToastMessage("친구 요청을 보냈습니다."))
                        changeRequestsUI(ids)
                    } else {
                        _addEvent.emit(
                            AddFriendViewModel.AddEvent.ShowToastMessage(
                                "친구 요청을 실패했습니다. " + ARchiveApplication.getString(
                                    R.string.reTryMessage
                                )
                            )
                        )
                    }
                }
            }
            _checkedList.emit(listOf())
        }
    }

    fun checkAddFriendList(friend: AddTagSearchFriendUIModel) {
        viewModelScope.launch {
            _addFriendListUI.value = addFriendListUI.value.map { item ->
                if (item.id == friend.id) {
                    item.isChecked = !item.isChecked
                    if (item.isChecked) {
                        _checkedList.value += item
                    } else {
                        _checkedList.value -= item
                    }
                }
                item
            }
        }
    }

    override fun searchTag() {
        viewModelScope.launch {
            friendsSearchUseCase(FriendsSearchRequest(tagT.value)).collect { result ->
                result.onSuccess { response ->
                    _addTagSearchFriendListUI.emit(listOf(response.toAddTagSearchFriendUIModel()))
                }.onFail {
                    if (it == 404) {

                    } else {
                        _addEvent.emit(
                            AddFriendViewModel.AddEvent.ShowToastMessage(
                                "사용자 검색에 실패했습니다. " + ARchiveApplication.getString(
                                    R.string.reTryMessage
                                )
                            )
                        )
                    }
                }
            }
        }
    }

    fun contactsSearch(phoneBooks: List<PhoneBooks>) {
        viewModelScope.launch {
            _addEvent.emit(AddFriendViewModel.AddEvent.OpenLoading)
            friendsSearchPhoneUseCase(FriendsSearchPhoneRequest(phoneBooks.filter {
                it.originPhone.length == 11 && it.originPhone.substring(0, 3) == "010"
            })).collect { result ->
                result.onSuccess { response ->
                    val uiModelList = response.friends.map { it.toAddTagSearchFriendUIModel() }
                    _addFriendList.emit(uiModelList)
                    _addFriendListUI.emit(uiModelList)
                    _addEvent.emit(AddFriendViewModel.AddEvent.CloseLoading)
                }.onFail {
                    _addEvent.emit(
                        AddFriendViewModel.AddEvent.ShowToastMessage(
                            "사용자 검색에 실패했습니다. " + ARchiveApplication.getString(
                                R.string.reTryMessage
                            )
                        )
                    )
                    _addEvent.emit(AddFriendViewModel.AddEvent.CloseLoading)
                }
            }
        }
    }

    fun searchFriend() {
        viewModelScope.launch {
            if (searchFriendText.value.isBlank()) {
                _addFriendListUI.emit(_addFriendList.value)
            } else {
                _addFriendListUI.emit(_addFriendList.value.filter {
                    it.nickname.contains(searchFriendText.value, ignoreCase = true) ||
                            it.name.contains(searchFriendText.value, ignoreCase = true)
                })
            }
        }
    }

    fun closeLoading() {
        viewModelScope.launch {
            _addEvent.emit(AddFriendViewModel.AddEvent.CloseLoading)
        }
    }

    private fun changeRequestsUI(friendIds:List<Long>){
        _addFriendListUI.value = addFriendListUI.value.map { item ->
            if (item.id in friendIds) {
                item.copy(
                    isFriendInviteToFriend = true,
                    isChecked = false
                )
            } else {
                item
            }
        }

    }

    private fun changeRequestUI(id: Long) {
        _addTagSearchFriendListUI.value = addTagSearchFriendListUI.value.map { item ->
            if (item.id == id) {
                item.copy(
                    isFriendInviteToFriend = true,
                )
            } else {
                item
            }
        }
    }

}