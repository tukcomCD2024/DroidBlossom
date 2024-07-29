package com.droidblossom.archive.presentation.ui.mypage

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.data.dto.common.PagingRequestDto
import com.droidblossom.archive.domain.model.member.MemberDetail
import com.droidblossom.archive.domain.usecase.group_capsule.MyGroupCapsulePageUseCase
import com.droidblossom.archive.domain.usecase.member.MemberUseCase
import com.droidblossom.archive.domain.usecase.open.MyPublicCapsulePageUseCase
import com.droidblossom.archive.domain.usecase.secret.MySecretCapsulePageUseCase
import com.droidblossom.archive.presentation.base.BaseViewModel
import com.droidblossom.archive.presentation.model.mypage.CapsuleData
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
class MyPageViewModelImpl @Inject constructor(
    private val memberUseCase: MemberUseCase,
    private val mySecretCapsulePageUseCase: MySecretCapsulePageUseCase,
    private val myPublicCapsulePageUseCase: MyPublicCapsulePageUseCase,
    private val myGroupCapsulePageUseCase: MyGroupCapsulePageUseCase
) : BaseViewModel(), MyPageViewModel {

    private val _myPageEvents = MutableSharedFlow<MyPageViewModel.MyPageEvent>()
    override val myPageEvents: SharedFlow<MyPageViewModel.MyPageEvent>
        get() = _myPageEvents.asSharedFlow()


    private val _myInfo =
        MutableStateFlow(MemberDetail("USER", "", "", "", "", "", 0, 0,
            tagSearchAvailable = false,
            phoneSearchAvailable = false
        ))
    override val myInfo: StateFlow<MemberDetail>
        get() = _myInfo

    private val _myCapsules = MutableStateFlow(listOf<CapsuleData>())
    override val myCapsules: StateFlow<List<CapsuleData>>
        get() = _myCapsules


    private val _hasNextPage = MutableStateFlow(true)
    override val hasNextPage: StateFlow<Boolean>
        get() = _hasNextPage
    private val _lastCreatedTime = MutableStateFlow(DateUtils.dataServerString)
    override val lastCreatedTime: StateFlow<String>
        get() = _lastCreatedTime

    private val _capsuleType = MutableStateFlow(MyPageFragment.SpinnerCapsuleType.SECRET)
    override val capsuleType: StateFlow<MyPageFragment.SpinnerCapsuleType>
        get() = _capsuleType

    private val scrollEventChannel = Channel<Unit>(Channel.CONFLATED)
    private val scrollEventFlow =
        scrollEventChannel.receiveAsFlow().throttleFirst(1000, TimeUnit.MILLISECONDS)

    private var getCapsuleListJob: Job? = null

    override var viewModelReload = false

    init {
        load()
        viewModelScope.launch {
            scrollEventFlow.collect {
                if (myCapsules.value.isEmpty()) {
                    getLatestCapsulePage()
                } else {
                    getCapsulePage()
                }
            }
        }
    }

    override fun load() {
        getMe()
        getLatestCapsulePage()
    }

    override fun onScrollNearBottom() {
        scrollEventChannel.trySend(Unit)
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

    override fun getLatestCapsulePage() {
        when (capsuleType.value) {
            MyPageFragment.SpinnerCapsuleType.SECRET -> {
                getLatestSecretCapsulePage()
            }

            MyPageFragment.SpinnerCapsuleType.PUBLIC -> {
                getLatestPublicCapsulePage()
            }

            MyPageFragment.SpinnerCapsuleType.GROUP -> {
                getLatestGroupCapsulePage()
            }
        }
    }

    private fun getSecretCapsulePage() {
        if (hasNextPage.value) {
            getCapsuleListJob?.cancel()
            getCapsuleListJob = viewModelScope.launch {
                mySecretCapsulePageUseCase(
                    PagingRequestDto(
                        15,
                        lastCreatedTime.value
                    )
                ).collect { result ->
                    result.onSuccess {
                        _hasNextPage.value = it.hasNext
                        _myCapsules.emit(myCapsules.value + it.capsules)
                        if (myCapsules.value.isNotEmpty()) {
                            _lastCreatedTime.value = myCapsules.value.last().createdDate
                        }
                    }.onFail {
                        myPageEvent(MyPageViewModel.MyPageEvent.ShowToastMessage("정보 불러오기 실패"))
                    }
                }
            }
        }
    }

    private fun getPublicCapsulePage() {
        if (hasNextPage.value) {
            getCapsuleListJob?.cancel()
            getCapsuleListJob = viewModelScope.launch {
                myPublicCapsulePageUseCase(
                    PagingRequestDto(
                        15,
                        lastCreatedTime.value
                    )
                ).collect { result ->
                    result.onSuccess {
                        _hasNextPage.value = it.hasNext
                        _myCapsules.emit(myCapsules.value + it.capsules)
                        if (myCapsules.value.isNotEmpty()) {
                            _lastCreatedTime.value = myCapsules.value.last().createdDate
                        }
                    }.onFail {
                        myPageEvent(MyPageViewModel.MyPageEvent.ShowToastMessage("정보 불러오기 실패"))
                    }
                }
            }
        }
    }

    private fun getGroupCapsulePage() {
        viewModelScope.launch {
            if (hasNextPage.value) {
                getCapsuleListJob?.cancel()
                getCapsuleListJob = viewModelScope.launch {
                    myGroupCapsulePageUseCase(
                        PagingRequestDto(
                            15,
                            lastCreatedTime.value
                        )
                    ).collect { result ->
                        result.onSuccess {
                            _hasNextPage.value = it.hasNext
                            _myCapsules.emit(myCapsules.value + it.capsules)
                            if (myCapsules.value.isNotEmpty()) {
                                _lastCreatedTime.value = myCapsules.value.last().createdDate
                            }
                        }.onFail {
                            myPageEvent(MyPageViewModel.MyPageEvent.ShowToastMessage("정보 불러오기 실패"))
                        }
                    }
                }
            }
        }
    }

    private fun getLatestSecretCapsulePage() {
        getCapsuleListJob?.cancel()
        getCapsuleListJob = viewModelScope.launch {
            mySecretCapsulePageUseCase(
                PagingRequestDto(
                    15,
                    DateUtils.dataServerString
                )
            ).collect { result ->
                result.onSuccess {
                    _hasNextPage.value = it.hasNext
                    _myCapsules.emit(it.capsules)
                    if (myCapsules.value.isNotEmpty()) {
                        _lastCreatedTime.value = myCapsules.value.last().createdDate
                    }
                }.onFail {
                    myPageEvent(MyPageViewModel.MyPageEvent.ShowToastMessage("정보 불러오기 실패"))
                }
            }
            myPageEvent(MyPageViewModel.MyPageEvent.SwipeRefreshLayoutDismissLoading)
        }
    }

    private fun getLatestPublicCapsulePage() {
        getCapsuleListJob?.cancel()
        getCapsuleListJob = viewModelScope.launch {
            myPublicCapsulePageUseCase(
                PagingRequestDto(
                    15,
                    DateUtils.dataServerString
                )
            ).collect { result ->
                result.onSuccess {
                    _hasNextPage.value = it.hasNext
                    _myCapsules.emit(it.capsules)
                    if (myCapsules.value.isNotEmpty()) {
                        _lastCreatedTime.value = myCapsules.value.last().createdDate
                    }
                }.onFail {
                    myPageEvent(MyPageViewModel.MyPageEvent.ShowToastMessage("정보 불러오기 실패"))
                }
            }
            myPageEvent(MyPageViewModel.MyPageEvent.SwipeRefreshLayoutDismissLoading)
        }
    }

    private fun getLatestGroupCapsulePage() {
        getCapsuleListJob?.cancel()
        getCapsuleListJob = viewModelScope.launch {
            myGroupCapsulePageUseCase(
                PagingRequestDto(
                    15,
                    DateUtils.dataServerString
                )
            ).collect { result ->
                result.onSuccess {
                    _hasNextPage.value = it.hasNext
                    _myCapsules.emit(it.capsules)
                    if (myCapsules.value.isNotEmpty()) {
                        _lastCreatedTime.value = myCapsules.value.last().createdDate
                    }
                }.onFail {
                    myPageEvent(MyPageViewModel.MyPageEvent.ShowToastMessage("정보 불러오기 실패"))
                }
            }
            myPageEvent(MyPageViewModel.MyPageEvent.SwipeRefreshLayoutDismissLoading)
        }
    }


    override fun updateCapsuleOpenState(capsuleIndex: Int, capsuleId: Long) {
        viewModelScope.launch {
            val newList = _myCapsules.value
            newList[capsuleIndex].isOpened = true
            _myCapsules.emit(newList)
        }
    }

    override fun deleteCapsule(capsuleIndex: Int, capsuleId: Long) {
        val currentList = _myCapsules.value.toMutableList()
        currentList.removeAt(_myCapsules.value.indexOfFirst { it.capsuleId == capsuleId })
        _myCapsules.value = currentList
    }


    override fun clickSetting() {
        viewModelScope.launch {
            myPageEvent(MyPageViewModel.MyPageEvent.ClickSetting)
        }
    }

    override fun selectSpinnerItem(item: MyPageFragment.SpinnerCapsuleType) {
        if (capsuleType.value != item) {
            viewModelReload = true
            _capsuleType.value = item
        }
    }
}