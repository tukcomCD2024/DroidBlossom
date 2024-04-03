package com.droidblossom.archive.presentation.ui.mypage.friend.addfriend

import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.R
import com.droidblossom.archive.domain.model.friend.FriendReqRequest
import com.droidblossom.archive.domain.model.friend.FriendsSearchRequest
import com.droidblossom.archive.domain.model.friend.FriendsSearchResponse
import com.droidblossom.archive.domain.usecase.friend.FriendsRequestUseCase
import com.droidblossom.archive.domain.usecase.friend.FriendsSearchUseCase
import com.droidblossom.archive.presentation.base.BaseViewModel
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddFriendViewModelImpl @Inject constructor(
    private val friendsSearchUseCase : FriendsSearchUseCase,
    private val friendsRequestUseCase: FriendsRequestUseCase
) : BaseViewModel(), AddFriendViewModel {

    private val _addEvent = MutableSharedFlow<AddFriendViewModel.AddEvent>()

    override val addEvent: SharedFlow<AddFriendViewModel.AddEvent>
        get() = _addEvent.asSharedFlow()

    //name

    private val _addFriendListUI = MutableStateFlow<List<FriendsSearchResponse>>(listOf())
    override val addFriendListUI: StateFlow<List<FriendsSearchResponse>>
        get() = _addFriendListUI

    private val _checkedList = MutableStateFlow<List<FriendsSearchResponse>>(listOf())
    override val checkedList: StateFlow<List<FriendsSearchResponse>>
        get() = _checkedList

    override val tagT: MutableStateFlow<String> = MutableStateFlow("")


    //Num

    private val _isSearchNumOpen = MutableStateFlow(false)
    override val isSearchNumOpen: StateFlow<Boolean>
        get() = _isSearchNumOpen

    override fun requestFriends() {
        viewModelScope.launch {
            checkedList.value.forEach { friend ->
                friendsRequestUseCase(FriendReqRequest(friendId = friend.id)).collect{ result ->
                    result.onSuccess {
                        _addEvent.emit(AddFriendViewModel.AddEvent.ShowToastMessage("친구 요청을 보냈습니다"))
                    }.onFail {
                        _addEvent.emit(AddFriendViewModel.AddEvent.ShowToastMessage("친구 요청 오류 발생"))
                    }
                }
            }
        }
    }

    fun checkAddFriendList(position : Int) {
        viewModelScope.launch {
            val newAddList = addFriendListUI.value
            if (newAddList[position].isChecked){
                newAddList[position].isChecked = false
                _checkedList.emit(newAddList.filter { it.isChecked })
            } else {
                newAddList[position].isChecked = true
                _checkedList.emit(newAddList.filter { it.isChecked })
            }
            _addFriendListUI.emit(newAddList)
        }
    }

    override fun searchTag() {
        viewModelScope.launch {
            friendsSearchUseCase(FriendsSearchRequest(tagT.value)).collect{ result ->
                result.onSuccess { response ->
                    _addFriendListUI.emit(listOf(response))
                }.onFail {
                    _addEvent.emit(AddFriendViewModel.AddEvent.ShowToastMessage("검색이 불가능 합니다."))
                }
            }
        }
    }

    fun resetList() {
        viewModelScope.launch {
            if (checkedList.value.isNotEmpty()) {
                _checkedList.emit(listOf())
            }
            if (addFriendListUI.value.isNotEmpty()){
                _addFriendListUI.emit(listOf())
            }
        }
    }

    //Name



    //Num
    override fun searchNum() {

    }

    override fun openSearchNum() {
        viewModelScope.launch {
            _isSearchNumOpen.emit(true)
        }
    }

    fun closeSearchNum(){
        viewModelScope.launch {
            _isSearchNumOpen.emit(false)
        }
    }
}