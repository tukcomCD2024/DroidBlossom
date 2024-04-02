package com.droidblossom.archive.presentation.ui.mypage.friend.addfriend

import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddFriendViewModelImpl @Inject constructor(

) : BaseViewModel(), AddFriendViewModel {

    private val _addEvent = MutableSharedFlow<AddFriendViewModel.AddEvent>()

    override val addEvent: SharedFlow<AddFriendViewModel.AddEvent>
        get() = _addEvent.asSharedFlow()

    private val _isSearchNumOpen = MutableStateFlow(false)
    override val isSearchNumOpen: StateFlow<Boolean>
        get() = _isSearchNumOpen

    override fun searchName() {

    }

    override fun openSearchName() {
        viewModelScope.launch {
            _isSearchNumOpen.emit(true)
        }
    }

}