package com.droidblossom.archive.presentation.ui.auth

import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModelImpl @Inject constructor() : BaseViewModel(), AuthViewModel {
    private val _doneEvent = MutableSharedFlow<AuthViewModel.AuthFlowEvent>()
    override val doneEvent: SharedFlow<AuthViewModel.AuthFlowEvent> = _doneEvent.asSharedFlow()

    // SignInFragment
    private val _signInState = MutableStateFlow(AuthViewModel.SignInState.SIGNOUT)
    override val signInState: StateFlow<AuthViewModel.SignInState> = _signInState

    private val _signInEvent = MutableSharedFlow<AuthViewModel.SignInResult>()
    override val signInEvent: SharedFlow<AuthViewModel.SignInResult> = _signInEvent.asSharedFlow()

    private val _signInSocial = MutableStateFlow<AuthViewModel.Social?>(null)
    override val signInSocial: StateFlow<AuthViewModel.Social?> = _signInSocial

    // SignUpFragment
    private val _phoneNumber = MutableStateFlow("")
    override val phoneNumber: MutableStateFlow<String> = _phoneNumber

    private val _rawPhoneNumber = _phoneNumber
        .map { it.replace("-", "") }
        .stateIn(viewModelScope, SharingStarted.Eagerly, "")
    override val rawPhoneNumber: StateFlow<String> = _rawPhoneNumber

    // CertificationFragment
    private val _remainTime = MutableStateFlow(300)
    override val remainTime: StateFlow<Int> = _remainTime
    private var isTimerStarted = false

    override val certificationNumber1 = MutableStateFlow("")
    override val certificationNumber2 = MutableStateFlow("")
    override val certificationNumber3 = MutableStateFlow("")
    override val certificationNumber4 = MutableStateFlow("")

    private val _certificationNumber: StateFlow<String> = combine(
        certificationNumber1,
        certificationNumber2,
        certificationNumber3,
        certificationNumber4
    ) { number1, number2, number3, number4 ->
        number1 + number2 + number3 + number4
    }.stateIn(viewModelScope, SharingStarted.Eagerly, "")
    override val certificationNumber: StateFlow<String> = _certificationNumber

    override fun initTimer() {
        _remainTime.value = 300
    }

    override fun startTimer() {
        if (!isTimerStarted) {
            isTimerStarted = true
            viewModelScope.launch {
                while (_remainTime.value > 0 && isActive) {
                    delay(1000)
                    _remainTime.value--
                }
            }
        }
    }

    override fun signInToSignUp() {
        viewModelScope.launch {
            _doneEvent.emit(AuthViewModel.AuthFlowEvent.SIGNIN_TO_SIGNUP)
        }
    }

    override fun signUpToCertification() {
        viewModelScope.launch {
            _doneEvent.emit(AuthViewModel.AuthFlowEvent.SIGNUP_TO_CERTIFICATION)
        }
    }

    override fun certificationToSignUpSuccess() {
        viewModelScope.launch {
            _doneEvent.emit(AuthViewModel.AuthFlowEvent.CERTIFICATION_TO_SIGNUPSUCCESS)
        }
    }

    override fun SignInSuccess(social : AuthViewModel.Social) {
        _signInState.value = AuthViewModel.SignInState.SIGNNIN
        _signInSocial.value = social
        viewModelScope.launch {
            _signInEvent.emit(AuthViewModel.SignInResult.SUCCESS)
        }
    }

    override fun SignInFail() {
        _signInState.value = AuthViewModel.SignInState.SIGNOUT
        viewModelScope.launch {
            _signInEvent.emit(AuthViewModel.SignInResult.FAIL)
        }
    }
}