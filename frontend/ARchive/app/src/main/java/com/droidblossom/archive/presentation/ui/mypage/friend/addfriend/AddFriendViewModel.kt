package com.droidblossom.archive.presentation.ui.mypage.friend.addfriend

import com.droidblossom.archive.domain.model.friend.FriendsSearchResponse
import com.droidblossom.archive.presentation.ui.mypage.MyPageViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface AddFriendViewModel {

    val addEvent : SharedFlow<AddEvent>

    //searchName
    val addFriendListUI : StateFlow<List<FriendsSearchResponse>>
    val checkedList : StateFlow<List<FriendsSearchResponse>>
    val tagT : MutableStateFlow<String>
    val searchFriendText: MutableStateFlow<String>
    //searchNum
    val isSearchNumOpen : StateFlow<Boolean>
    val addFriendList: StateFlow<List<FriendsSearchResponse>>


    fun requestFriends()
    //searchName
    fun searchTag()

    //searchNum
    fun searchNum()
    fun openSearchNum()

    sealed class AddEvent {
        data class ShowToastMessage(val message : String) : AddEvent()
        object  OpenLoading : AddEvent()
        object  CloseLoading : AddEvent()
        object NotificationChange : AddEvent()
    }
}