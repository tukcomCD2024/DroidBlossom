package com.droidblossom.archive.presentation.ui.skin

import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.data.dto.capsule_skin.request.CapsuleSkinsPageRequestDto
import com.droidblossom.archive.domain.model.common.CapsuleSkinSummary
import com.droidblossom.archive.domain.usecase.capsule_skin.CapsuleSkinsPageUseCase
import com.droidblossom.archive.presentation.base.BaseViewModel
import com.droidblossom.archive.util.DateUtils
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SkinViewModelImpl @Inject constructor(
    private val capsuleSkinsPageUseCase: CapsuleSkinsPageUseCase
):BaseViewModel(), SkinViewModel {

    private val _skinEvents = MutableSharedFlow<SkinViewModel.SkinEvent>()
    override val skinEvents: SharedFlow<SkinViewModel.SkinEvent>
        get() = _skinEvents.asSharedFlow()

    private val _skins = MutableStateFlow(listOf<CapsuleSkinSummary>())

    override val skins: StateFlow<List<CapsuleSkinSummary>>
        get() = _skins

    private val _lastCreatedSkinTime = MutableStateFlow(DateUtils.dataServerString)
    override val lastCreatedSkinTime: StateFlow<String>
        get() = _lastCreatedSkinTime

    private val _hasNextSkins = MutableStateFlow(true)
    override val hasNextSkins: StateFlow<Boolean>
        get() = _hasNextSkins

    private val _isSearchOpen = MutableStateFlow(false)
    override val isSearchOpen: StateFlow<Boolean>
        get() = _isSearchOpen


    override fun getSkinList() {
        viewModelScope.launch {
            if (hasNextSkins.value) {
                capsuleSkinsPageUseCase(
                    CapsuleSkinsPageRequestDto(
                        15,
                        _lastCreatedSkinTime.value
                    )
                ).collect { result ->
                    result.onSuccess {
                        _skins.emit(it.skins)
                        _hasNextSkins.emit(it.hasNext)
                        _lastCreatedSkinTime.emit(it.skins.last().createdAt)
                    }.onFail {
                        _skinEvents.emit(SkinViewModel.SkinEvent.ShowToastMessage("스킨 불러오기 실패."))
                    }

                }
            }
        }
    }

    override fun goSkinMake() {
        viewModelScope.launch {
            _skinEvents.emit(SkinViewModel.SkinEvent.ToSkinMake)
        }
    }

    override fun openSearchSkin() {
        viewModelScope.launch {
            _isSearchOpen.emit(true)
        }
    }

    override fun closeSearchSkin() {
        viewModelScope.launch {
            _isSearchOpen.emit(false)
        }
    }
}