package com.droidblossom.archive.presentation.ui.social.page.group

import kotlinx.coroutines.flow.StateFlow

interface SocialGroupViewModel {

    val isSearchOpen : StateFlow<Boolean>

    fun openSearchGroupCapsule()
    fun closeSearchGroupCapsule()

    fun searchGroupCapsule()
}