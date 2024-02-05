package com.droidblossom.archive.presentation.ui.mypage

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.domain.model.common.MyCapsule
import com.droidblossom.archive.domain.model.member.MemberDetail
import com.droidblossom.archive.domain.model.secret.SecretCapsulePageRequest
import com.droidblossom.archive.domain.usecase.member.MemberUseCase
import com.droidblossom.archive.domain.usecase.secret.SecretCapsulePageUseCase
import com.droidblossom.archive.presentation.base.BaseViewModel
import com.droidblossom.archive.presentation.ui.auth.AuthViewModel
import com.droidblossom.archive.util.DateUtils
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModelImpl @Inject constructor(
    private val memberUseCase: MemberUseCase,
    private val secretCapsulePageUseCase: SecretCapsulePageUseCase
) : BaseViewModel(), MyPageViewModel {

    private val _myPageEvents = MutableSharedFlow<MyPageViewModel.MyPageEvent>()
    override val myPageEvents: SharedFlow<MyPageViewModel.MyPageEvent>
        get() =_myPageEvents.asSharedFlow()


            private val _myInfo = MutableStateFlow(MemberDetail("USER", "", ""))
    override val myInfo: StateFlow<MemberDetail>
        get() = _myInfo

    private val _myCapsules = MutableStateFlow(listOf<MyCapsule>())
    override val myCapsules: StateFlow<List<MyCapsule>>
        get() = _myCapsules

    private val _hasNextPage = MutableStateFlow(true)
    override val hasNextPage: StateFlow<Boolean>
        get() = _hasNextPage
    private val _lastCreatedTime = MutableStateFlow(DateUtils.dataServerString)
    override val lastCreatedTime: StateFlow<String>
        get() = _lastCreatedTime


    override fun getMe() {
        viewModelScope.launch {
            memberUseCase().collect { result ->
                result.onSuccess {
                    _myInfo.emit(it)
                }.onFail {
                    _myPageEvents.emit(MyPageViewModel.MyPageEvent.ShowToastMessage("정보 불러오기 실패"))
                }
            }
        }
    }

    override fun getSecretCapsulePage() {
        viewModelScope.launch {
            if (hasNextPage.value) {
                secretCapsulePageUseCase(
                    SecretCapsulePageRequest(
                        15,
                        lastCreatedTime.value
                    ).toDto()
                ).collect { result ->
                    result.onSuccess {
                        _hasNextPage.value = it.hasNext
                        _myCapsules.emit(myCapsules.value + it.capsules)
                        _lastCreatedTime.value = _myCapsules.value.last().createdAt
                    }.onFail {
                        _myPageEvents.emit(MyPageViewModel.MyPageEvent.ShowToastMessage("정보 불러오기 실패"))
                    }
                }
            }
        }
    }

    override fun clearCapsules() {
        viewModelScope.launch {
            _myCapsules.value = listOf()
            _lastCreatedTime.value = DateUtils.dataServerString
            _hasNextPage.value = true
            getSecretCapsulePage()
        }
    }
}