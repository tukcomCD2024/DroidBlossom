package com.droidblossom.archive.presentation.ui.mypage.friendaccept

import com.droidblossom.archive.presentation.base.BaseViewModel
import com.droidblossom.archive.presentation.ui.mypage.friend.FriendViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class FriendAcceptViewModelImpl @Inject constructor(
) : BaseViewModel(), FriendAcceptViewModel {

    private val _friendAcceptEvent = MutableSharedFlow<FriendViewModel.FriendEvent>()
    override val friendAcceptEvent: SharedFlow<FriendViewModel.FriendEvent>
        get() = _friendAcceptEvent.asSharedFlow()


}