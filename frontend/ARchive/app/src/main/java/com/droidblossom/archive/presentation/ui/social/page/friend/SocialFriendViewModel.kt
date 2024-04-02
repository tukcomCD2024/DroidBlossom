package com.droidblossom.archive.presentation.ui.social.page.friend

import kotlinx.coroutines.flow.StateFlow

interface SocialFriendViewModel {

    val isSearchOpen : StateFlow<Boolean>

    fun openSearchFriendCapsule()
    fun closeSearchFriendCapsule()

    fun searchFriendCapsule()
}