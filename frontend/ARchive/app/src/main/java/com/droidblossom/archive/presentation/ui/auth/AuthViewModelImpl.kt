package com.droidblossom.archive.presentation.ui.auth

import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.domain.model.auth.SignIn
import com.droidblossom.archive.domain.model.auth.SignUp
import com.droidblossom.archive.domain.model.auth.VerificationMessageSend
import com.droidblossom.archive.domain.model.auth.VerificationNumberValid
import com.droidblossom.archive.domain.model.member.CheckStatus
import com.droidblossom.archive.domain.usecase.member.MemberStatusUseCase
import com.droidblossom.archive.domain.usecase.auth.ReIssueUseCase
import com.droidblossom.archive.domain.usecase.auth.SendMessageUseCase
import com.droidblossom.archive.domain.usecase.auth.SignInUseCase
import com.droidblossom.archive.domain.usecase.auth.SignUpUseCase
import com.droidblossom.archive.domain.usecase.auth.TemporaryTokenReIssueUseCase
import com.droidblossom.archive.domain.usecase.auth.ValidMessageUseCase
import com.droidblossom.archive.domain.usecase.member.ChangePhoneSendMessageUseCase
import com.droidblossom.archive.domain.usecase.member.ChangePhoneValidMessageUseCase
import com.droidblossom.archive.presentation.base.BaseViewModel
import com.droidblossom.archive.util.DataStoreUtils
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
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
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class AuthViewModelImpl @Inject constructor(
    private val reIssueUseCase: ReIssueUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val validMessageUseCase: ValidMessageUseCase,
    private val memberStatusUseCase: MemberStatusUseCase,
    private val temporaryTokenReIssueUseCase: TemporaryTokenReIssueUseCase,
    private val changePhoneSendMessageUseCase: ChangePhoneSendMessageUseCase,
    private val changePhoneValidMessageUseCase: ChangePhoneValidMessageUseCase,
    private val dataStoreUtils: DataStoreUtils,
) : BaseViewModel(), AuthViewModel {

    private val _isPhoneChange = MutableStateFlow<Boolean>(false)
    override val isPhoneChange: StateFlow<Boolean>
        get() = _isPhoneChange

    // SignInFragment
    private val _signInEvents = MutableSharedFlow<AuthViewModel.SignInEvent>()
    override val signInEvents: SharedFlow<AuthViewModel.SignInEvent> = _signInEvents.asSharedFlow()

    // UserAgreementFragment
    private val _userAgreementEvents = MutableSharedFlow<AuthViewModel.UserAgreementEvent>()
    override val userAgreementEvents: SharedFlow<AuthViewModel.UserAgreementEvent> = _userAgreementEvents.asSharedFlow()

    private val _privacyAgreementState = MutableStateFlow(false)
    override val privacyAgreementState: StateFlow<Boolean>
        get() = _privacyAgreementState

    private val _serviceAgreementState = MutableStateFlow(false)
    override val serviceAgreementState: StateFlow<Boolean>
        get() = _serviceAgreementState

    private val _locationAgreementState = MutableStateFlow(false)
    override val locationAgreementState: StateFlow<Boolean>
        get() = _locationAgreementState

    private val _ageVerificationState = MutableStateFlow(false)
    override val ageVerificationState: StateFlow<Boolean>
        get() = _ageVerificationState

    private val _fullAgreementCheckState: StateFlow<Boolean> = combine(
        privacyAgreementState,
        serviceAgreementState,
        locationAgreementState,
        ageVerificationState
    ) { privacyAgreement, serviceAgreement, locationAgreement, ageVerification ->
        privacyAgreement && serviceAgreement && locationAgreement && ageVerification
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)
    override val fullAgreementCheckState: StateFlow<Boolean>
        get() = _fullAgreementCheckState

    // SignUpFragment
    private val _signUpEvents = MutableSharedFlow<AuthViewModel.SignUpEvent>()
    override val signUpEvents: SharedFlow<AuthViewModel.SignUpEvent> = _signUpEvents.asSharedFlow()

    override val phoneNumber = MutableStateFlow("")

    private val _rawPhoneNumber = phoneNumber
        .map { it.replace("-", "") }
        .stateIn(viewModelScope, SharingStarted.Eagerly, "")
    override val rawPhoneNumber: StateFlow<String> = _rawPhoneNumber

    private var appHash: String = ""


    // CertificationFragment
    private val _certificationEvents = MutableSharedFlow<AuthViewModel.CertificationEvent>()
    override val certificationEvents: SharedFlow<AuthViewModel.CertificationEvent> =
        _certificationEvents.asSharedFlow()

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

    fun changePhoneMode() {
        viewModelScope.launch {
            _isPhoneChange.emit(true)
        }
    }

    // SignInFragment
    override fun signInEvent(event: AuthViewModel.SignInEvent) {
        viewModelScope.launch {
            _signInEvents.emit(event)
        }
    }

    // UserAgreementFragment
    override fun userAgreementEvent(event: AuthViewModel.UserAgreementEvent) {
        viewModelScope.launch {
            _userAgreementEvents.emit(event)
        }
    }
    override fun onFullAgreementClick(){
        val setState = !fullAgreementCheckState.value
        _privacyAgreementState.value = setState
        _serviceAgreementState.value = setState
        _locationAgreementState.value = setState
        _ageVerificationState.value = setState
    }
    override fun onPrivacyAgreementClick(){
        _privacyAgreementState.value = !_privacyAgreementState.value
    }
    override fun onServiceAgreementClick(){
        _serviceAgreementState.value = !_serviceAgreementState.value
    }
    override fun onLocationAgreementClick(){
        _locationAgreementState.value = !_locationAgreementState.value
    }
    override fun onAgeVerificationClick(){
        _ageVerificationState.value = !_ageVerificationState.value
    }

    override fun onAgreeClick() {
        if (fullAgreementCheckState.value){
            userAgreementEvent(AuthViewModel.UserAgreementEvent.NavigateToSignUp)
        }else{
            userAgreementEvent(AuthViewModel.UserAgreementEvent.ShowToastMessage("필수 조건을 모두 동의해야 회원가입을 진행할 수 있습니다."))
        }
    }

    private fun authDataReset() {
        certificationNumber4.value = ""
        certificationNumber3.value = ""
        certificationNumber2.value = ""
        certificationNumber1.value = ""
        _privacyAgreementState.value = false
        _serviceAgreementState.value = false
        _locationAgreementState.value = false
        _ageVerificationState.value = false
        phoneNumber.value = ""
    }

    override fun memberStatusCheck(memberStatusCheckData: CheckStatus, signUpData: SignUp) {
        viewModelScope.launch {
            memberStatusUseCase(memberStatusCheckData.toDto()).collect { result ->
                result.onSuccess { it ->
                    if (it.isDeleted){
                        signInEvent(AuthViewModel.SignInEvent.DeactivatedUserChecked)
                    }else{
                        if (it.isVerified) {
                            submitSignInData(
                                SignIn(
                                    memberStatusCheckData.authId,
                                    memberStatusCheckData.socialType
                                )
                            )
                        } else if (it.isExist) {
                            getTemporaryToken(
                                SignIn(
                                    memberStatusCheckData.authId,
                                    memberStatusCheckData.socialType
                                )
                            )
                        } else {
                            submitSignUpData(signUpData)
                        }
                    }
                    authDataReset()
                }.onFail {
                    signInEvent(AuthViewModel.SignInEvent.ShowToastMessage("죄송합니다. 문제가 발생했습니다. 다시 시도해주세요"))
                }
            }
        }
    }

    override fun submitSignInData(signInData: SignIn) {
        viewModelScope.launch {
            signInUseCase(signInData.toDto()).collect { result ->
                result.onSuccess {
                    // 토큰 저장 로직 추가
                    dataStoreUtils.resetTokenData()
                    signInEvent(AuthViewModel.SignInEvent.NavigateToMain)
                    dataStoreUtils.saveAccessToken(it.accessToken)
                    dataStoreUtils.saveRefreshToken(it.refreshToken)
                }.onFail {
                    signInEvent(AuthViewModel.SignInEvent.ShowToastMessage("죄송합니다. 문제가 발생했습니다. 다시 시도해주세요"))
                }
            }
        }
    }

    override fun submitSignUpData(signUpData: SignUp) {
        viewModelScope.launch {
            signUpUseCase(signUpData.toDto()).collect { result ->
                result.onSuccess {
                    dataStoreUtils.resetTokenData()
                    dataStoreUtils.saveAccessToken(it.temporaryAccessToken)
                    signInEvent(AuthViewModel.SignInEvent.NavigateToUserAgreement)
                }.onFail {
                    signInEvent(AuthViewModel.SignInEvent.ShowToastMessage("죄송합니다. 문제가 발생했습니다. 다시 시도해주세요"))
                }
            }
        }
    }

    override fun getTemporaryToken(temporaryTokenReIssue: SignIn) {
        viewModelScope.launch {
            temporaryTokenReIssueUseCase(temporaryTokenReIssue.toDto()).collect { result ->
                result.onSuccess {
                    dataStoreUtils.resetTokenData()
                    dataStoreUtils.saveAccessToken(it.temporaryAccessToken)
                    signInEvent(AuthViewModel.SignInEvent.NavigateToUserAgreement)
                }.onFail {
                    signInEvent(AuthViewModel.SignInEvent.ShowToastMessage("죄송합니다. 문제가 발생했습니다. 다시 시도해주세요"))
                }
            }
        }
    }

    // SignUpFragment
    override fun signUpEvent(event: AuthViewModel.SignUpEvent) {
        viewModelScope.launch {
            _signUpEvents.emit(event)
        }
    }

    override fun setHash(hash: String) {
        appHash = hash
    }

    override fun checkPhoneNumber(): Boolean {
        val pattern = "^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$"
        if (!Pattern.matches(pattern, rawPhoneNumber.value)) {
            signUpEvent(AuthViewModel.SignUpEvent.ShowToastMessage("올바른 휴대폰 번호를 입력해주세요."))
            return false
        }
        return true
    }

    override fun clearPhoneNumber() {
        viewModelScope.launch {
            phoneNumber.emit("")
        }
    }

    override fun submitPhoneNumber() {
        // 임시토큰 헤더에 넣고 해야함.
        viewModelScope.launch {
            if (isPhoneChange.value) {
                changePhoneSendMessageUseCase(
                    VerificationMessageSend(rawPhoneNumber.value, appHash).toDto()
                ).collect { result ->
                    result.onSuccess {
                        signUpEvent(AuthViewModel.SignUpEvent.NavigateToCertification)
                    }.onFail {
                        when (it) {
                            429 -> {
                                signUpEvent(AuthViewModel.SignUpEvent.ShowToastMessage("인증 문자 발송 횟수를 초과하였습니다. 24시간 이후에 시도해 주세요."))
                                certificationEvent(AuthViewModel.CertificationEvent.ShowToastMessage("하루 인증 문자 발송 횟수를 초과하였습니다. 내일 다시 시도해 주세요."))
                            }
                            400 -> {
                                signUpEvent(AuthViewModel.SignUpEvent.ShowToastMessage("이미 사용중인 휴대폰 번호입니다."))
                            }
                            else -> {
                                signUpEvent(AuthViewModel.SignUpEvent.ShowToastMessage("죄송합니다. 문제가 발생했습니다. 다시 시도해주세요"))
                            }
                        }
                    }
                }
            } else {
                sendMessageUseCase(
                    VerificationMessageSend(rawPhoneNumber.value, appHash).toDto()
                ).collect { result ->
                    result.onSuccess {
                        signUpEvent(AuthViewModel.SignUpEvent.NavigateToCertification)
                    }.onFail {
                        when (it) {
                            429 -> {
                                signUpEvent(AuthViewModel.SignUpEvent.ShowToastMessage("인증 문자 발송 횟수를 초과하였습니다. 24시간 이후에 시도해 주세요."))
                                certificationEvent(AuthViewModel.CertificationEvent.ShowToastMessage("하루 인증 문자 발송 횟수를 초과하였습니다. 내일 다시 시도해 주세요."))
                            }
                            400 -> {
                                signUpEvent(AuthViewModel.SignUpEvent.ShowToastMessage("이미 사용중인 휴대폰 번호입니다."))
                            }
                            else -> {
                                signUpEvent(AuthViewModel.SignUpEvent.ShowToastMessage("죄송합니다. 문제가 발생했습니다. 다시 시도해주세요"))
                            }
                        }
                    }
                }
            }
        }
    }

    // CertificationFragment
    override fun certificationEvent(event: AuthViewModel.CertificationEvent) {
        viewModelScope.launch {
            _certificationEvents.emit(event)
        }
    }

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

    override fun submitCertificationNumber() {
        viewModelScope.launch {
            if (isPhoneChange.value) {
                changePhoneValidMessageUseCase(
                    VerificationNumberValid(certificationNumber.value, rawPhoneNumber.value).toDto()
                ).collect { result ->
                    result.onSuccess {
                        certificationEvent(AuthViewModel.CertificationEvent.NavigateFinish)

                    }.onFail {
                        // Toast 메시지 있으면 좋을듯
                        if (it == 400) {
                            certificationEvent(AuthViewModel.CertificationEvent.ShowToastMessage("인증번호가 일치하지 않습니다."))
                            certificationEvent(AuthViewModel.CertificationEvent.VerificationCodeMismatch)
                        }else{
                            certificationEvent(AuthViewModel.CertificationEvent.ShowToastMessage("죄송합니다. 문제가 발생했습니다. 다시 시도해주세요"))
                        }

                    }
                }
            } else {
                validMessageUseCase(
                    VerificationNumberValid(certificationNumber.value, rawPhoneNumber.value).toDto()
                ).collect { result ->
                    result.onSuccess {
                        dataStoreUtils.saveAccessToken(it.accessToken)
                        dataStoreUtils.saveRefreshToken(it.refreshToken)
                        certificationEvent(AuthViewModel.CertificationEvent.NavigateToSignUpSuccess)

                    }.onFail {
                        // Toast 메시지 있으면 좋을듯
                        if (it == 400) {
                            certificationEvent(AuthViewModel.CertificationEvent.ShowToastMessage("인증번호가 일치하지 않습니다."))
                            certificationEvent(AuthViewModel.CertificationEvent.VerificationCodeMismatch)
                        }else{
                            certificationEvent(AuthViewModel.CertificationEvent.ShowToastMessage("죄송합니다. 문제가 발생했습니다. 다시 시도해주세요"))
                        }

                    }
                }
            }
        }
    }

    override fun reSend() {
        initTimer()
        submitPhoneNumber()
    }

}