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
class AuthViewModel @Inject constructor() : BaseViewModel(){

    private val _doneEvent = MutableSharedFlow<AuthFlowEvent>()
    val doneEvent: SharedFlow<AuthFlowEvent> = _doneEvent.asSharedFlow()

    // SignUpFragment
    val phoneNumber = MutableStateFlow("")

    private val _rawPhoneNumber = phoneNumber
        .map { it.replace("-", "") }
        .stateIn(viewModelScope, SharingStarted.Eagerly, "")

    val rawPhoneNumber: StateFlow<String> = _rawPhoneNumber


    // CertificationFragment
    val remainTime = MutableStateFlow(300)
    private var isTimerStarted = false


    val certificationNumber1= MutableStateFlow("")
    val certificationNumber2= MutableStateFlow("")
    val certificationNumber3= MutableStateFlow("")
    val certificationNumber4= MutableStateFlow("")

    private val _certificationNumber: StateFlow<String> = combine(
        certificationNumber1,
        certificationNumber2,
        certificationNumber3,
        certificationNumber4
    ) { number1, number2, number3, number4 ->
        number1 + number2 + number3 + number4
    }.stateIn(viewModelScope, SharingStarted.Eagerly, "")

    val certificationNumber : StateFlow<String> = _certificationNumber


    fun initTimer(){
        remainTime.value = 300
    }

    fun startTimer() {
        if (!isTimerStarted) {
            isTimerStarted = true
            viewModelScope.launch {
                while (remainTime.value > 0 && isActive) {
                    delay(1000)
                    remainTime.value--
                }
            }
        }
    }



    fun signInToSignUp() {
        viewModelScope.launch {
            _doneEvent.emit(AuthFlowEvent.SIGNIN_TO_SIGNUP)
        }
    }

    fun signUpToCertification() {
        viewModelScope.launch {
            _doneEvent.emit(AuthFlowEvent.SIGNUP_TO_CERTIFICATION)
        }
    }

    fun certificationToSignUpSuccess() {
        viewModelScope.launch {
            _doneEvent.emit(AuthFlowEvent.CERTIFICATION_TO_SIGNUPSUCCESS)
        }
    }

    enum class AuthFlowEvent {
        SIGNIN_TO_SIGNUP,
        SIGNUP_TO_CERTIFICATION,
        CERTIFICATION_TO_SIGNUPSUCCESS,
    }

}