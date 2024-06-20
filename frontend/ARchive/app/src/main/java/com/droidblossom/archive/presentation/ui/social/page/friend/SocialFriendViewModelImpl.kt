package com.droidblossom.archive.presentation.ui.social.page.friend

import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.data.dto.common.PagingRequestDto
import com.droidblossom.archive.domain.model.common.SocialCapsules
import com.droidblossom.archive.domain.usecase.open.PublicCapsulePageUseCase
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
class SocialFriendViewModelImpl @Inject constructor(
    private val publicCapsulePageUseCase: PublicCapsulePageUseCase
) : BaseViewModel(), SocialFriendViewModel {

    private val _socialFriendEvents = MutableSharedFlow<SocialFriendViewModel.SocialFriendEvent>()
    override val socialFriendEvents: SharedFlow<SocialFriendViewModel.SocialFriendEvent>
        get() = _socialFriendEvents.asSharedFlow()

    private val _publicCapsules = MutableStateFlow(listOf<SocialCapsules>())
    override val publicCapsules: StateFlow<List<SocialCapsules>>
        get() = _publicCapsules

    private val _isSearchOpen = MutableStateFlow(false)
    override val isSearchOpen: StateFlow<Boolean>
        get() = _isSearchOpen

    private val _hasNextPage = MutableStateFlow(true)
    override val hasNextPage: StateFlow<Boolean>
        get() = _hasNextPage
    private val _lastCreatedTime = MutableStateFlow(DateUtils.dataServerString)
    override val lastCreatedTime: StateFlow<String>
        get() = _lastCreatedTime

    private val scrollEventChannel = Channel<Unit>(Channel.CONFLATED)
    private val scrollEventFlow =
        scrollEventChannel.receiveAsFlow().throttleFirst(1000, TimeUnit.MILLISECONDS)

    private var getPublicCapsuleListJob: Job? = null


    init {
        viewModelScope.launch {
            scrollEventFlow.collect {
                if (publicCapsules.value.isEmpty()){
                    getLatestPublicCapsule()
                }else{
                    getPublicCapsulePage()
                }
            }
        }
        getLatestPublicCapsule()
    }

    override fun onScrollNearBottom() {
        scrollEventChannel.trySend(Unit)
    }

    override fun socialFriendEvent(event: SocialFriendViewModel.SocialFriendEvent) {
        viewModelScope.launch {
            _socialFriendEvents.emit(event)
        }
    }

    override fun openSearchFriendCapsule() {
        viewModelScope.launch {
            _isSearchOpen.emit(true)
        }
    }

    override fun closeSearchFriendCapsule() {
        viewModelScope.launch {
            _isSearchOpen.emit(false)
        }
    }


    override fun getPublicCapsulePage() {
        if (hasNextPage.value){
            getPublicCapsuleListJob?.cancel()
            getPublicCapsuleListJob = viewModelScope.launch {
                publicCapsulePageUseCase(
                    PagingRequestDto(
                        15,
                        lastCreatedTime.value
                    )
                ).collect { result ->
                    result.onSuccess {
                        _hasNextPage.value = it.hasNext
                        _publicCapsules.emit(publicCapsules.value + it.publicCapsules)
                        if (publicCapsules.value.isNotEmpty()) {
                            _lastCreatedTime.value = it.publicCapsules.last().createdDate
                        }
                    }.onFail {
                        socialFriendEvent(SocialFriendViewModel.SocialFriendEvent.ShowToastMessage("공개캡슐 불러오기 실패"))
                    }
                }
            }
        }
    }

    override fun getLatestPublicCapsule() {
        getPublicCapsuleListJob?.cancel()
        getPublicCapsuleListJob = viewModelScope.launch {
            publicCapsulePageUseCase(
                PagingRequestDto(
                    15,
                    DateUtils.dataServerString
                )
            ).collect { result ->
                result.onSuccess {
                    _hasNextPage.value = it.hasNext
                    _publicCapsules.value = it.publicCapsules
                    if (publicCapsules.value.isNotEmpty()) {
                        _lastCreatedTime.value = it.publicCapsules.last().createdDate
                    }
                }.onFail {
                    socialFriendEvent(SocialFriendViewModel.SocialFriendEvent.ShowToastMessage("공개캡슐 불러오기 실패"))
                }
            }
            socialFriendEvent(SocialFriendViewModel.SocialFriendEvent.SwipeRefreshLayoutDismissLoading)
        }
    }

    override fun searchFriendCapsule() {

    }

}