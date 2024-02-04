package com.droidblossom.archive.presentation.ui.mypage

import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.domain.model.member.MemberDetail
import com.droidblossom.archive.domain.usecase.member.MemberUseCase
import com.droidblossom.archive.presentation.base.BaseViewModel
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModelImpl @Inject constructor(
    private val memberUseCase: MemberUseCase
) : BaseViewModel(), MyPageViewModel {

    private val _myInfo = MutableStateFlow(MemberDetail("USER","",""))

    override val myInfo: StateFlow<MemberDetail>
        get() = _myInfo


    override fun getMe() {
        viewModelScope.launch {
            memberUseCase().collect{ result ->
                result.onSuccess {
                    _myInfo.emit(it)
                }.onFail {

                }
            }
        }
    }
}