package com.droidblossom.archive.presentation.ui.skin

import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.ARchiveApplication.Companion.getString
import com.droidblossom.archive.R
import com.droidblossom.archive.data.dto.common.PagingRequestDto
import com.droidblossom.archive.domain.model.common.CapsuleSkinSummary
import com.droidblossom.archive.domain.usecase.capsule_skin.CapsuleSkinsPageUseCase
import com.droidblossom.archive.presentation.base.BaseViewModel
import com.droidblossom.archive.util.DateUtils
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class SkinViewModelImpl @Inject constructor(
    private val capsuleSkinsPageUseCase: CapsuleSkinsPageUseCase
) : BaseViewModel(), SkinViewModel {

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

    private val scrollEventChannel = Channel<Unit>(Channel.CONFLATED)
    private val scrollEventFlow =
        scrollEventChannel.receiveAsFlow().throttleFirst(1000, TimeUnit.MILLISECONDS)

    private var getSkinLstJob: Job? = null

    init {
        viewModelScope.launch {
            scrollEventFlow.collect {
                if (skins.value.isEmpty()) {
                    getLatestSkinList()
                } else {
                    getSkinList()
                }
            }
        }
    }

    override fun onScrollNearBottom() {
        scrollEventChannel.trySend(Unit)
    }


    override fun getSkinList() {
        if (hasNextSkins.value) {
            getSkinLstJob?.cancel()
            getSkinLstJob = viewModelScope.launch {
                capsuleSkinsPageUseCase(
                    PagingRequestDto(
                        15,
                        _lastCreatedSkinTime.value
                    )
                ).collect { result ->
                    result.onSuccess {
                        _hasNextSkins.value = it.hasNext
                        _skins.value = skins.value + it.skins
                        if (skins.value.isNotEmpty()) {
                            _lastCreatedSkinTime.value = skins.value.last().createdAt
                        }
                    }.onFail {
                        _skinEvents.emit(
                            SkinViewModel.SkinEvent.ShowToastMessage(
                                "스킨 목록을 불러오는데 실패했습니다. " + getString(
                                    R.string.reTryMessage
                                )
                            )
                        )
                    }

                }
            }
        }
    }

    override fun getLatestSkinList() {
        getSkinLstJob?.cancel()
        getSkinLstJob = viewModelScope.launch {
            capsuleSkinsPageUseCase(
                PagingRequestDto(
                    15,
                    DateUtils.dataServerString
                )
            ).collect { result ->
                result.onSuccess {
                    _hasNextSkins.value = it.hasNext
                    _skins.value = it.skins
                    if (skins.value.isNotEmpty()) {
                        _lastCreatedSkinTime.value = skins.value.last().createdAt
                    }
                }.onFail {
                    _skinEvents.emit(
                        SkinViewModel.SkinEvent.ShowToastMessage(
                            "스킨 목록을 불러오는데 실패했습니다. " + getString(
                                R.string.reTryMessage
                            )
                        )
                    )
                }

            }
            _skinEvents.emit(SkinViewModel.SkinEvent.SwipeRefreshLayoutDismissLoading)
        }
    }


    override fun goSkinMake() {
        viewModelScope.launch {
            _skinEvents.emit(SkinViewModel.SkinEvent.ToSkinMake)
        }
    }

    override fun searchSkin() {
        viewModelScope.launch {
            //스킨 검색 API
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

    override fun deleteSkin(skinId: Long) {
        skins.value.find { it.id == skinId }?.let { skin ->
            _skins.value -= skin
        }
    }
}