package com.droidblossom.archive.presentation.ui.skin.detail

import com.droidblossom.archive.domain.model.common.CapsuleSkinSummary
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface SkinDetailViewModel {

    val skinDetailEvents: SharedFlow<SkinDetailEvent>
    val skin: StateFlow<CapsuleSkinSummary>

    fun skinDetailEvent(event:SkinDetailEvent)
    fun setSkin(skin:CapsuleSkinSummary)

    fun deleteSkin()

    sealed class SkinDetailEvent {
        object ModifySkin : SkinDetailEvent()
        object DeleteSkin : SkinDetailEvent()
        data class ShowToastMessage(val message: String) : SkinDetailEvent()
    }
}