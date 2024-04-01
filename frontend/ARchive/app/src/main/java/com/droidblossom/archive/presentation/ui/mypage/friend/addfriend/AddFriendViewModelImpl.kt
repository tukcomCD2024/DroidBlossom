package com.droidblossom.archive.presentation.ui.mypage.friend.addfriend

import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddFriendViewModelImpl @Inject constructor(

) : BaseViewModel(), AddFriendViewModel {


    private val _isSearchNameOpen = MutableStateFlow(false)
    override val isSearchNameOpen: StateFlow<Boolean>
        get() = _isSearchNameOpen

    override fun searchName() {

    }

    override fun openSearchName() {
        viewModelScope.launch {
            _isSearchNameOpen.emit(true)
        }
    }
}