package com.droidblossom.archive.presentation.ui.social.page.group

import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.data.dto.open.request.PublicCapsuleSliceRequestDto
import com.droidblossom.archive.domain.model.common.SocialCapsules
import com.droidblossom.archive.presentation.base.BaseViewModel
import com.droidblossom.archive.presentation.ui.social.page.friend.SocialFriendViewModel
import com.droidblossom.archive.util.DateUtils
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
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

    private val _groupCapsules = MutableStateFlow(listOf<SocialCapsules>())
    override val groupCapsules: StateFlow<List<SocialCapsules>>
        get() = _groupCapsules

    private val _hasNextPage = MutableStateFlow(true)
    override val hasNextPage: StateFlow<Boolean>
        get() = _hasNextPage
    private val _lastCreatedTime = MutableStateFlow(DateUtils.dataServerString)
    override val lastCreatedTime: StateFlow<String>
        get() = _lastCreatedTime

    init {
        //getGroupCapsulePage()
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
                /*
                publicCapsulePageUseCase(
                    PublicCapsuleSliceRequestDto(
                        15,
                        lastCreatedTime.value
                    )
                ).collect { result ->
                    result.onSuccess {
                        _hasNextPage.value = it.hasNext
                        _publicCapsules.emit(publicCapsules.value + it.publicCapsules)
                        _lastCreatedTime.value = publicCapsules.value.last().createdDate
                    }.onFail {
                        socialFriendEvent(SocialFriendViewModel.SocialFriendEvent.ShowToastMessage("공개캡슐 불러오기 실패"))
                    }
                }

                */
            }
        }
    }

    override fun getLatestGroupCapsule(){
        viewModelScope.launch {
            /*
            publicCapsulePageUseCase(
                PublicCapsuleSliceRequestDto(
                    15,
                    DateUtils.dataServerString
                )
            ).collect { result ->
                result.onSuccess {
                    _hasNextPage.value = it.hasNext
                    _publicCapsules.emit(it.publicCapsules)
                    _lastCreatedTime.value = publicCapsules.value.last().createdDate
                }.onFail {
                    socialFriendEvent(SocialFriendViewModel.SocialFriendEvent.ShowToastMessage("공개캡슐 불러오기 실패"))
                }
            }

             */
        }
    }

}