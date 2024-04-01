package com.droidblossom.archive.presentation.ui.mypage.friend.addfriend

import kotlinx.coroutines.flow.StateFlow

interface AddFriendViewModel {

    val isSearchNameOpen : StateFlow<Boolean>
    fun searchName()
    fun openSearchName()
}