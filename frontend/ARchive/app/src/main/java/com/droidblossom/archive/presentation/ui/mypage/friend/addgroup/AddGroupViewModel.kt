package com.droidblossom.archive.presentation.ui.mypage.friend.addgroup

import android.net.Uri
import com.droidblossom.archive.domain.model.friend.FriendsSearchResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface AddGroupViewModel {

    val addGroupEvent: SharedFlow<AddGroupEvent>

    val groupTitle: MutableStateFlow<String>
    val groupContent: MutableStateFlow<String>
    val groupProfileUri : MutableStateFlow<Uri?>

    val isCollapse : StateFlow<Boolean>
    val isFriendSearchOpen: StateFlow<Boolean>
    val friendList: StateFlow<List<FriendsSearchResponse>>
    val checkedList : StateFlow<List<FriendsSearchResponse>>

    fun expandedAppBar()
    fun collapsedAppBar()
    fun search()
    fun openSearch()
    fun closeSearch()

    fun getFriendList()

    sealed class AddGroupEvent {
        data class ShowToastMessage(val message: String) : AddGroupEvent()
    }
}