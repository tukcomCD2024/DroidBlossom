package com.droidblossom.archive.presentation.ui.social.page.group

import com.droidblossom.archive.domain.model.common.SocialCapsules
import kotlinx.coroutines.flow.StateFlow

interface SocialGroupViewModel {

    val groupCapsules : StateFlow<List<SocialCapsules>>
    val isSearchOpen : StateFlow<Boolean>
    val hasNextPage : StateFlow<Boolean>
    val lastCreatedTime : StateFlow<String>

    fun openSearchGroupCapsule()
    fun closeSearchGroupCapsule()

    fun searchGroupCapsule()

    fun getGroupCapsulePage()

    fun getLatestGroupCapsule()
}