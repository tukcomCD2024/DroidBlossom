package com.droidblossom.archive.presentation.ui.auth

import com.droidblossom.archive.domain.model.auth.SignIn
import com.droidblossom.archive.domain.model.auth.SignUp
import com.droidblossom.archive.domain.model.member.CheckStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface AuthViewModel {

    // SignIn
    val signInEvents: SharedFlow<SignInEvent>

    // SignUp
    val signUpEvents: SharedFlow<SignUpEvent>

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

    fun memberStatusCheck(memberStatusCheckData : CheckStatus, signUpData : SignUp)

    fun submitSignInData(signInData : SignIn)
    fun submitSignUpData(signUpData : SignUp)

    fun getTemporaryToken(temporaryTokenReIssue : SignIn)

    // SignUp
    fun signUpEvent(event: SignUpEvent)

    fun checkPhoneNumber(): Boolean
    fun setHash(hash : String)
    fun submitPhoneNumber()

    // Certification
    fun initTimer()
    fun startTimer()
    fun certificationEvent(event: CertificationEvent)
    fun reSend()
    fun submitCertificationNumber()

    sealed class SignInEvent {
        data class SocialSignSuccess(val signUpData : SignUp) : SignInEvent()
        data class SignInFailure(val error: Throwable) : SignInEvent()
        object NavigateToSignUp : SignInEvent()
        object NavigateToMain : SignInEvent()

    }

    sealed class SignUpEvent {

        // data class로 바꿔야함
        object SendPhoneNumber : SignUpEvent()
        object NavigateToCertification : SignUpEvent()
    }

    sealed class CertificationEvent {
        // data class로 바꿔야함
        object SubmitCertificationCode : CertificationEvent()
        object ReSend : CertificationEvent()
        object NavigateToSignUpSuccess : CertificationEvent()

        object failCertificationCode : CertificationEvent()
    }

    enum class Social{
        GOOGLE,
        KAKAO
    }
}