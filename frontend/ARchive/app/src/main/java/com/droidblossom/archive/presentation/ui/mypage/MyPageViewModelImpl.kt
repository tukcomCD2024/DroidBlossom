package com.droidblossom.archive.presentation.ui.mypage

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.domain.model.member.MemberDetail
import com.droidblossom.archive.domain.model.secret.SecretCapsulePageRequest
import com.droidblossom.archive.domain.usecase.member.MemberUseCase
import com.droidblossom.archive.domain.usecase.secret.SecretCapsulePageUseCase
import com.droidblossom.archive.presentation.base.BaseViewModel
import com.droidblossom.archive.presentation.model.mypage.CapsuleData
import com.droidblossom.archive.util.DateUtils
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MyPageViewModelImpl @Inject constructor(
    private val memberUseCase: MemberUseCase,
    private val secretCapsulePageUseCase: SecretCapsulePageUseCase
) : BaseViewModel(), MyPageViewModel {

    private val _myPageEvents = MutableSharedFlow<MyPageViewModel.MyPageEvent>()
    override val myPageEvents: SharedFlow<MyPageViewModel.MyPageEvent>
        get() = _myPageEvents.asSharedFlow()


    private val _myInfo = MutableStateFlow(MemberDetail("USER", "", "", 0, 0))
    override val myInfo: StateFlow<MemberDetail>
        get() = _myInfo

    private val _myCapsules = MutableStateFlow(listOf<CapsuleData>())
    override val myCapsules: StateFlow<List<CapsuleData>>
        get() = _myCapsules

    private val _myCapsulesUI = MutableStateFlow(listOf<CapsuleData>())
    override val myCapsulesUI: StateFlow<List<CapsuleData>>
        get() = _myCapsulesUI

    private val _hasNextPage = MutableStateFlow(true)
    override val hasNextPage: StateFlow<Boolean>
        get() = _hasNextPage
    private val _lastCreatedTime = MutableStateFlow(DateUtils.dataServerString)
    override val lastCreatedTime: StateFlow<String>
        get() = _lastCreatedTime

    private val _capsuleType = MutableStateFlow(MyPageFragment.SpinnerCapsuleType.SECRET)
    override val capsuleType: StateFlow<MyPageFragment.SpinnerCapsuleType>
        get() = _capsuleType

    override var reloadMyInfo = false
    override var clearCapsule = false

    init {
        getMe()
    }
    override fun load() {
        getMe()
        clearCapsules(true)
    }

    override fun myPageEvent(event: MyPageViewModel.MyPageEvent) {
        viewModelScope.launch {
            _myPageEvents.emit(event)
        }
    }


    override fun getMe() {
        viewModelScope.launch {
            memberUseCase().collect { result ->
                result.onSuccess {
                    _myInfo.emit(it)
                    reloadMyInfo = false
                }.onFail {
                    myPageEvent(MyPageViewModel.MyPageEvent.ShowToastMessage("정보 불러오기 실패"))
                }
            }
        }
    }

    override fun getCapsulePage() {
        when (capsuleType.value) {
            MyPageFragment.SpinnerCapsuleType.SECRET -> {
                getSecretCapsulePage()
            }

            MyPageFragment.SpinnerCapsuleType.PUBLIC -> {
                getPublicCapsulePage()
            }

            MyPageFragment.SpinnerCapsuleType.GROUP -> {
                getGroupCapsulePage()
            }
        }
    }

    private fun getSecretCapsulePage() {
        viewModelScope.launch {
            if (hasNextPage.value) {
                secretCapsulePageUseCase(
                    SecretCapsulePageRequest(
                        15,
                        lastCreatedTime.value
                    ).toDto()
                ).collect { result ->
                    result.onSuccess {
                        withContext(Dispatchers.Default) {
                            val currentIds = myCapsules.value.map { capsule -> capsule.capsuleId }.toSet()
                            val newCapsules = it.capsules.filter { capsule -> capsule.capsuleId !in currentIds }
                            Log.d("뭐냐", "${myCapsules.value + newCapsules}")
                            withContext(Dispatchers.Main) {
                                if (myCapsules.value.isEmpty()) {
                                    _myCapsules.emit(newCapsules)
                                } else {
                                    _myCapsules.emit(myCapsules.value + newCapsules)
                                }
                                _hasNextPage.value = it.hasNext
                                _lastCreatedTime.value = myCapsules.value.last().createdDate
                            }
                        }
                    }.onFail {
                        myPageEvent(MyPageViewModel.MyPageEvent.ShowToastMessage("정보 불러오기 실패"))
                    }
                }
                myPageEvent(MyPageViewModel.MyPageEvent.HideLoading)
            }
        }
    }

    private fun getPublicCapsulePage() {
        viewModelScope.launch {
            if (hasNextPage.value) {
                _myCapsules.emit(listOf())
                myPageEvent(MyPageViewModel.MyPageEvent.HideLoading)
            }
        }
    }

    private fun getGroupCapsulePage() {
        viewModelScope.launch {
            if (hasNextPage.value) {
                _myCapsules.emit(listOf())
                myPageEvent(MyPageViewModel.MyPageEvent.HideLoading)
            }
        }
    }


    override fun updateMyCapsulesUI() {
        viewModelScope.launch {
            _myCapsulesUI.emit(myCapsules.value)
        }
    }

    override fun clearCapsules(setting:Boolean) {
        clearCapsule = setting
        viewModelScope.launch {
            _myCapsules.value = listOf()
            _lastCreatedTime.value = DateUtils.dataServerString
            _hasNextPage.value = true
            getCapsulePage()
        }
    }

    override fun updateCapsuleOpenState(capsuleIndex: Int, capsuleId: Long) {
        viewModelScope.launch {
            val newList = _myCapsules.value
            newList[capsuleIndex].isOpened = true
            _myCapsules.emit(newList)
            _myCapsulesUI.emit(myCapsules.value)
        }
    }

    override fun clickSetting() {
        viewModelScope.launch {
            myPageEvent(MyPageViewModel.MyPageEvent.ClickSetting)
        }
    }

    override fun selectSpinnerItem(item: MyPageFragment.SpinnerCapsuleType) {
        _capsuleType.value = item
    }
}