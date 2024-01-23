package com.droidblossom.archive.presentation.ui.auth

import com.droidblossom.archive.domain.model.auth.SignUp
import com.droidblossom.archive.domain.model.member.CheckStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface AuthViewModel {

    // SignIn
    val signInEvents: SharedFlow<SignInEvent>

    // SignUp
    val signUpEvents: SharedFlow<SigUpnEvent>

    // Certification
    val certificationEvents: SharedFlow<CertificationEvent>

    val phoneNumber: MutableStateFlow<String>
    val rawPhoneNumber: StateFlow<String>

    val remainTime: StateFlow<Int>

    val certificationNumber: StateFlow<String>
    val certificationNumber1: MutableStateFlow<String>
    val certificationNumber2: MutableStateFlow<String>
    val certificationNumber3: MutableStateFlow<String>
    val certificationNumber4: MutableStateFlow<String>


    // SignIn
    fun signInEvent(event: SignInEvent)

    fun memberStatusCheck(memberStatusCheckData : CheckStatus)

    // SignUp
    fun initTimer()
    fun startTimer()
    fun signUpEvent(event: SigUpnEvent)

    // Certification
    fun certificationEvent(event: CertificationEvent)

    sealed class SignInEvent {
        data class SocialSignSuccess(val signUpData : SignUp) : SignInEvent()
        data class SignInFailure(val error: Throwable) : SignInEvent()
        object NavigateToSignUp : SignInEvent()
        object NavigateToMain : SignInEvent()

    }

    sealed class SigUpnEvent {

        // data class로 바꿔야함
        object SendPhoneNumber : SigUpnEvent()
        object NavigateToCertification : SigUpnEvent()
    }

    sealed class CertificationEvent {
        // data class로 바꿔야함
        object SubmitCertificationCode : CertificationEvent()
        object ReSend : CertificationEvent()
        object NavigateToSignUpSuccess : CertificationEvent()
    }

    enum class Social{
        GOOGLE,
        KAKAO
    }
}