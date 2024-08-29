package com.droidblossom.archive.presentation.ui.auth

import com.droidblossom.archive.domain.model.auth.SignIn
import com.droidblossom.archive.domain.model.auth.SignUp
import com.droidblossom.archive.domain.model.member.CheckStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface AuthViewModel {

    val isPhoneChange : StateFlow<Boolean>

    // SignIn
    val signInEvents: SharedFlow<SignInEvent>

    // UserAgreement
    val userAgreementEvents: SharedFlow<UserAgreementEvent>
    val fullAgreementCheckState: StateFlow<Boolean>
    val privacyAgreementState: StateFlow<Boolean>
    val serviceAgreementState: StateFlow<Boolean>
    val locationAgreementState: StateFlow<Boolean>
    val ageVerificationState: StateFlow<Boolean>

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

    // UserAgreement
    fun userAgreementEvent(event: UserAgreementEvent)
    fun onFullAgreementClick()
    fun onPrivacyAgreementClick()
    fun onServiceAgreementClick()
    fun onLocationAgreementClick()
    fun onAgeVerificationClick()
    fun onAgreeClick()

    // SignUp
    fun signUpEvent(event: SignUpEvent)

    fun checkPhoneNumber(): Boolean
    fun setHash(hash : String)
    fun submitPhoneNumber()

    fun clearPhoneNumber()

    // Certification
    fun initTimer()
    fun startTimer()
    fun certificationEvent(event: CertificationEvent)
    fun reSend()
    fun submitCertificationNumber()

    sealed class SignInEvent {
        object NavigateToUserAgreement : SignInEvent()
        object NavigateToMain : SignInEvent()
        object DeactivatedUserChecked : SignInEvent()
        data class ShowToastMessage(val message : String) : SignInEvent()

    }

    sealed class UserAgreementEvent {
        object NavigateToSignUp : UserAgreementEvent()
        data class ShowToastMessage(val message : String) : UserAgreementEvent()

    }


    sealed class SignUpEvent {
        object NavigateToCertification : SignUpEvent()
        object ActivityFinish: SignUpEvent()
        data class ShowToastMessage(val message : String) : SignUpEvent()

    }

    sealed class CertificationEvent {
        object NavigateToSignUpSuccess : CertificationEvent()

        object VerificationCodeMismatch : CertificationEvent()
        object NavigateFinish : CertificationEvent()
        data class ShowToastMessage(val message : String) : CertificationEvent()

    }

    enum class Social{
        GOOGLE,
        KAKAO
    }
}