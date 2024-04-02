package com.droidblossom.archive.presentation.ui.mypage.friend.addfriend

import com.droidblossom.archive.presentation.ui.mypage.MyPageViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface AddFriendViewModel {

    val addEvent : SharedFlow<AddEvent>
    val isSearchNumOpen : StateFlow<Boolean>
    fun searchName()
    fun openSearchName()

    sealed class AddEvent {
        data class ShowToastMessage(val message : String) : AddEvent()
    }
}