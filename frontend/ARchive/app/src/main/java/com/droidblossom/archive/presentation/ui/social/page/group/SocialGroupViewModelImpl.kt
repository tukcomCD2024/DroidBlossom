package com.droidblossom.archive.presentation.ui.social.page.group

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.data.dto.common.IdBasedPagingRequestDto
import com.droidblossom.archive.data.dto.common.PagingRequestDto
import com.droidblossom.archive.domain.model.common.SocialCapsules
import com.droidblossom.archive.domain.usecase.group_capsule.CapsulesOfGroupsPageUseCase
import com.droidblossom.archive.presentation.base.BaseViewModel
import com.droidblossom.archive.presentation.base.BaseViewModel.Companion.throttleFirst
import com.droidblossom.archive.presentation.ui.social.page.friend.SocialFriendViewModel
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
class SocialGroupViewModelImpl @Inject constructor(
    private val getCapsulesOfGroupsPageUseCase: CapsulesOfGroupsPageUseCase
): BaseViewModel(), SocialGroupViewModel {

    private val _socialGroupEvents = MutableSharedFlow<SocialGroupViewModel.SocialGroupEvent>()
    override val socialGroupEvents: SharedFlow<SocialGroupViewModel.SocialGroupEvent>
        get() =_socialGroupEvents.asSharedFlow()

    private val _isSearchOpen = MutableStateFlow(false)
    override val isSearchOpen: StateFlow<Boolean>
        get() = _isSearchOpen

    private val _groupCapsules = MutableStateFlow(listOf<SocialCapsules>())
    override val groupCapsules: StateFlow<List<SocialCapsules>>
        get() = _groupCapsules

    private val _hasNextPage = MutableStateFlow(true)
    override val hasNextPage: StateFlow<Boolean>
        get() = _hasNextPage
    private val _lastCreatedTime = MutableStateFlow(DateUtils.dataServerString)
    override val lastCreatedTime: StateFlow<String>
        get() = _lastCreatedTime

    private val scrollEventChannel = Channel<Unit>(Channel.CONFLATED)
    private val scrollEventFlow =
        scrollEventChannel.receiveAsFlow().throttleFirst(1000, TimeUnit.MILLISECONDS)

    private var getGroupCapsuleListJob: Job? = null
    init {
        viewModelScope.launch {
            scrollEventFlow.collect {
                if (groupCapsules.value.isEmpty()){
                    getLatestGroupCapsule()
                }else{
                    getGroupCapsulePage()
                }
            }
        }
        getLatestGroupCapsule()
    }

    override fun onScrollNearBottom() {
        scrollEventChannel.trySend(Unit)
    }

    override fun socialGroupEvent(event: SocialGroupViewModel.SocialGroupEvent) {
        viewModelScope.launch {
            _socialGroupEvents.emit(event)
        }
    }

    override fun openSearchGroupCapsule() {
        viewModelScope.launch {
            _isSearchOpen.emit(true)
        }
    }

    override fun closeSearchGroupCapsule() {
        viewModelScope.launch {
            _isSearchOpen.emit(false)
        }
    }

    override fun searchGroupCapsule(){

    }

    override fun getGroupCapsulePage(){
        viewModelScope.launch {
            if (hasNextPage.value) {
                getGroupCapsuleListJob?.cancel()
                getGroupCapsuleListJob = viewModelScope.launch {
                    getCapsulesOfGroupsPageUseCase(
                        IdBasedPagingRequestDto(
                            15,
                            groupCapsules.value.lastOrNull()?.capsuleId
                        )
                    ).collect { result ->
                        result.onSuccess {
                            _hasNextPage.value = it.hasNext
                            _groupCapsules.emit(groupCapsules.value + it.groupCapsules)
                        }.onFail {
                            socialGroupEvent(SocialGroupViewModel.SocialGroupEvent.ShowToastMessage("그룹캡슐 불러오기 실패"))
                        }

                    }
                }
            }
        }
    }

    override fun getLatestGroupCapsule(){
        getGroupCapsuleListJob?.cancel()
        getGroupCapsuleListJob = viewModelScope.launch {
            getGroupCapsuleListJob = viewModelScope.launch {
                getCapsulesOfGroupsPageUseCase(
                    IdBasedPagingRequestDto(
                        15,
                        null
                    )
                ).collect { result ->
                    result.onSuccess {
                        Log.d("머여",it.toString())
                        _hasNextPage.value = it.hasNext
                        _groupCapsules.value = it.groupCapsules
                    }.onFail {
                        socialGroupEvent(SocialGroupViewModel.SocialGroupEvent.ShowToastMessage("그룹캡슐 불러오기 실패"))
                    }
                }
                socialGroupEvent(SocialGroupViewModel.SocialGroupEvent.SwipeRefreshLayoutDismissLoading)
            }
        }
    }

    override fun deleteCapsule(capsuleIndex: Int, capsuleId: Long) {
        _groupCapsules.value.find { it.capsuleId == capsuleId }?.let { capsule ->
            _groupCapsules.value -= capsule
        }
    }

}