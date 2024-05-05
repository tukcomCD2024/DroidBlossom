package com.droidblossom.archive.presentation.ui.social.page.friend

import com.droidblossom.archive.domain.model.common.SocialCapsules
import com.droidblossom.archive.presentation.ui.mypage.MyPageViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface SocialFriendViewModel {
    val socialFriendEvents : SharedFlow<SocialFriendEvent>
    val publicCapsules : StateFlow<List<SocialCapsules>>
    val isSearchOpen : StateFlow<Boolean>
    val hasNextPage : StateFlow<Boolean>
    val lastCreatedTime : StateFlow<String>
    var clearCapsule:Boolean


    fun socialFriendEvent(event: SocialFriendEvent)
    fun openSearchFriendCapsule()
    fun closeSearchFriendCapsule()

    fun searchFriendCapsule()
    fun getPublicCapsulePage()
    fun onScrollNearBottom()
    fun getLatestPublicCapsule()
    sealed class SocialFriendEvent{
        data class ShowToastMessage(val message : String) : SocialFriendEvent()
        object HideLoading : SocialFriendEvent()

    }
}