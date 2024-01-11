package com.droidblossom.archive.presentation.ui.auth

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface AuthViewModel {
    val doneEvent: SharedFlow<AuthFlowEvent>
    val phoneNumber: MutableStateFlow<String>

    val singInState : StateFlow<SingInState>
    val singInEvent : SharedFlow<SignInResult>

    val rawPhoneNumber: StateFlow<String>
    val remainTime: StateFlow<Int>
    val certificationNumber: StateFlow<String>

    val certificationNumber1: MutableStateFlow<String>
    val certificationNumber2: MutableStateFlow<String>
    val certificationNumber3: MutableStateFlow<String>
    val certificationNumber4: MutableStateFlow<String>

    fun initTimer()
    fun startTimer()
    fun signInToSignUp()
    fun signUpToCertification()
    fun certificationToSignUpSuccess()

    fun SignInSuccess()
    fun SignInFail()

    enum class AuthFlowEvent {
        SIGNIN_TO_SIGNUP,
        SIGNUP_TO_CERTIFICATION,
        CERTIFICATION_TO_SIGNUPSUCCESS,
    }

    enum class SingInState {
        SIGININ,
        SIGNOUT
    }

    enum class SignInResult{
        SUCCESS,
        FAIL
    }
}