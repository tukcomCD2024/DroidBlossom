package com.droidblossom.archive.presentation.ui.social.page.group

import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.domain.model.common.SocialCapsules
import com.droidblossom.archive.presentation.base.BaseViewModel
import com.droidblossom.archive.presentation.ui.social.page.friend.SocialFriendViewModel
import com.droidblossom.archive.util.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SocialGroupViewModelImpl @Inject constructor(

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
    override var clearCapsule = false

    init {
        //getGroupCapsulePage()
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

            }
        }
    }

    override fun getLatestGroupCapsule(){
        clearCapsule = true
        _groupCapsules.value = listOf()
        _hasNextPage.value = true
        _lastCreatedTime.value = DateUtils.dataServerString
        getGroupCapsulePage()
    }

}