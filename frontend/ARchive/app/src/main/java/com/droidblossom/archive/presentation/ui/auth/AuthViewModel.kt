package com.droidblossom.archive.presentation.ui.auth

import com.droidblossom.archive.domain.model.auth.SignUp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface AuthViewModel {
    val doneEvent: SharedFlow<AuthFlowEvent>
    val phoneNumber: MutableStateFlow<String>

    val signInEvents: SharedFlow<SignInEvent>

    sealed class SignInEvent {
        data class SocialSignSuccess(val signUpData : SignUp) : SignInEvent()
        data class SignInFailure(val error: Throwable) : SignInEvent()
        object NavigateToSignUp : SignInEvent()
        object NavigateToMain : SignInEvent()
    }

    fun signInEvent(event: SignInEvent)

    val rawPhoneNumber: StateFlow<String>
    val remainTime: StateFlow<Int>
    val certificationNumber: StateFlow<String>

    val certificationNumber1: MutableStateFlow<String>
    val certificationNumber2: MutableStateFlow<String>
    val certificationNumber3: MutableStateFlow<String>
    val certificationNumber4: MutableStateFlow<String>


    val certificationEvents: SharedFlow<CertificationEvent>

    sealed class CertificationEvent {
        // data class로 바꾸어야 함
        object submitCertificationCode : CertificationEvent()
        object reSend : CertificationEvent()
        object NavigateToSignUpSuccess : CertificationEvent()
    }

    fun certificationEvent(event: CertificationEvent)


    fun initTimer()
    fun startTimer()
    fun signInToSignUp()
    fun signUpToCertification()
    fun certificationToSignUpSuccess()

    val signUpEvents: SharedFlow<SigUpnEvent>
    sealed class SigUpnEvent {

        // data class로 바꿔야함
        object SendPhoneNumber : SigUpnEvent()
        object NavigateToCertification : SigUpnEvent()
    }
    fun signUpEvent(event: SigUpnEvent)

    enum class AuthFlowEvent {
        SIGNIN_TO_SIGNUP,
        SIGNUP_TO_CERTIFICATION,
        CERTIFICATION_TO_SIGNUPSUCCESS,
    }

    enum class SignInState {
        SIGNNIN,
        SIGNOUT
    }

    enum class SignInResult{
        SUCCESS,
        FAIL
    }

    enum class Social{
        GOOGLE,
        KAKAO
    }
}