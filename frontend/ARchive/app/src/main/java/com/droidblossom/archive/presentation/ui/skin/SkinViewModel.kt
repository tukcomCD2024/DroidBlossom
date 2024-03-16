package com.droidblossom.archive.presentation.ui.skin

import com.droidblossom.archive.domain.model.common.CapsuleSkinSummary
import com.droidblossom.archive.presentation.ui.home.createcapsule.CreateCapsuleViewModel
import com.droidblossom.archive.util.DateUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface SkinViewModel {
    val skinEvents : SharedFlow<SkinEvent>

    val skins: StateFlow<List<CapsuleSkinSummary>>
    val lastCreatedSkinTime: StateFlow<String>
    val hasNextSkins: StateFlow<Boolean>
    val isSearchOpen : StateFlow<Boolean>
    val skinsUI: StateFlow<List<CapsuleSkinSummary>>
    fun getSkinList()
    fun goSkinMake()
    fun searchSkin()
    fun openSearchSkin()
    fun closeSearchSkin()

    fun updateMySkinsUI()
    fun clearSkins()

    sealed class SkinEvent {
        object ToSkinMake : SkinEvent()
        data class ShowToastMessage(val message : String) : SkinEvent()

    }

}