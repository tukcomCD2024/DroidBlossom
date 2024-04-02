package com.droidblossom.archive.presentation.ui.social.page.group

import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SocialGroupViewModelImpl @Inject constructor(

): BaseViewModel(), SocialGroupViewModel {


    private val _isSearchOpen = MutableStateFlow(false)
    override val isSearchOpen: StateFlow<Boolean>
        get() = _isSearchOpen

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

}