package com.droidblossom.archive.presentation.ui.social.page.group

import com.droidblossom.archive.domain.model.common.SocialCapsules
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface SocialGroupViewModel {

    val socialGroupEvents : SharedFlow<SocialGroupEvent>
    val groupCapsules : StateFlow<List<SocialCapsules>>
    val isSearchOpen : StateFlow<Boolean>
    val hasNextPage : StateFlow<Boolean>
    val lastCreatedTime : StateFlow<String>

    fun socialGroupEvent(event: SocialGroupEvent)


    fun openSearchGroupCapsule()
    fun closeSearchGroupCapsule()

    fun searchGroupCapsule()

    fun getGroupCapsulePage()

    fun getLatestGroupCapsule()
    fun onScrollNearBottom()

    fun deleteCapsule(capsuleIndex: Int, capsuleId: Long)

    sealed class SocialGroupEvent{
        data class ShowToastMessage(val message : String) : SocialGroupEvent()
        object SwipeRefreshLayoutDismissLoading : SocialGroupEvent()

    }
}