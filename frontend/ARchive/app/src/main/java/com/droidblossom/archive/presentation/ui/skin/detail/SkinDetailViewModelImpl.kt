package com.droidblossom.archive.presentation.ui.skin.detail

import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.domain.model.common.CapsuleSkinSummary
import com.droidblossom.archive.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SkinDetailViewModelImpl @Inject constructor(
    
): BaseViewModel(), SkinDetailViewModel {
    
    private val _skinDetailEvents = MutableSharedFlow<SkinDetailViewModel.SkinDetailEvent>()
    override val skinDetailEvents: SharedFlow<SkinDetailViewModel.SkinDetailEvent>
        get() = _skinDetailEvents.asSharedFlow()
    
    private val _skin = MutableStateFlow(CapsuleSkinSummary(
        id = 0,
        skinUrl = "",
        name = "",
        createdAt = "",
        isClicked = false
    ))
    override val skin: StateFlow<CapsuleSkinSummary>
        get() = _skin

    override fun skinDetailEvent(event: SkinDetailViewModel.SkinDetailEvent) {
        viewModelScope.launch {
            _skinDetailEvents.emit(event)
        }
    }

    override fun setSkin(skin: CapsuleSkinSummary) {
        _skin.value = skin
    }
}
