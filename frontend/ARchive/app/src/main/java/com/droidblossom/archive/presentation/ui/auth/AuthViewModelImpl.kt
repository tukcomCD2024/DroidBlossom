package com.droidblossom.archive.presentation.ui.auth

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.ARchiveApplication
import com.droidblossom.archive.domain.model.auth.SignIn
import com.droidblossom.archive.domain.model.auth.SignUp
import com.droidblossom.archive.domain.model.auth.VerificationMessageSend
import com.droidblossom.archive.domain.model.auth.VerificationNumberValid
import com.droidblossom.archive.domain.model.member.CheckStatus
import com.droidblossom.archive.domain.usecase.MemberStatusUseCase
import com.droidblossom.archive.domain.usecase.ReIssueUseCase
import com.droidblossom.archive.domain.usecase.SendMessageUseCase
import com.droidblossom.archive.domain.usecase.SignInUseCase
import com.droidblossom.archive.domain.usecase.SignUpUseCase
import com.droidblossom.archive.domain.usecase.TemporaryTokenReIssueUseCase
import com.droidblossom.archive.domain.usecase.ValidMessageUseCase
import com.droidblossom.archive.presentation.base.BaseViewModel
import com.droidblossom.archive.util.SharedPreferencesUtils
import com.droidblossom.archive.util.onError
import com.droidblossom.archive.util.onException
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
    private val signUpUseCase : SignUpUseCase,
    private val validMessageUseCase: ValidMessageUseCase,
    private val memberStatusUseCase: MemberStatusUseCase,
    private val temporaryTokenReIssueUseCase: TemporaryTokenReIssueUseCase,
    private val sharedPreferencesUtils : SharedPreferencesUtils
) : BaseViewModel(), AuthViewModel {

    // SignInFragment
    private val _signInEvents = MutableSharedFlow<AuthViewModel.SignInEvent>()
    override val signInEvents: SharedFlow<AuthViewModel.SignInEvent> = _signInEvents.asSharedFlow()

    // SignUpFragment
    private val _signUpEvents = MutableSharedFlow<AuthViewModel.SignUpEvent>()
    override val signUpEvents: SharedFlow<AuthViewModel.SignUpEvent> = _signUpEvents.asSharedFlow()

    private val _phoneNumber = MutableStateFlow("")
    override val phoneNumber: MutableStateFlow<String> = _phoneNumber

    private val _rawPhoneNumber = _phoneNumber
        .map { it.replace("-", "") }
        .stateIn(viewModelScope, SharingStarted.Eagerly, "")
    override val rawPhoneNumber: StateFlow<String> = _rawPhoneNumber

    private var appHash : String = ""



    // CertificationFragment
    private val _certificationEvents =  MutableSharedFlow<AuthViewModel.CertificationEvent>()
    override val certificationEvents: SharedFlow<AuthViewModel.CertificationEvent> = _certificationEvents.asSharedFlow()

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

    // SignInFragment
    override fun signInEvent(event: AuthViewModel.SignInEvent) {
        viewModelScope.launch {
            _signInEvents.emit(event)
        }
    }

    override fun memberStatusCheck(memberStatusCheckData : CheckStatus, signUpData : SignUp){
        viewModelScope.launch {
            memberStatusUseCase(memberStatusCheckData.toDto()).collect{ result ->
                result.onSuccess {it ->
                    if (it.isVerified){
                        submitSignInData(SignIn(memberStatusCheckData.authId, memberStatusCheckData.socialType))
                    }else if(it.isExist){
                        getTemporaryToken(SignIn(memberStatusCheckData.authId, memberStatusCheckData.socialType))
                    }else{
                        submitSignUpData(signUpData)
                    }
                }
            }
        }
    }

    override fun submitSignInData(signInData : SignIn){
        viewModelScope.launch {
            signInUseCase(signInData.toDto()).collect{ result ->
                result.onSuccess {
                    // 토큰 저장 로직 추가
                    ARchiveApplication.sp.resetTokenData()
                    signInEvent(AuthViewModel.SignInEvent.NavigateToMain)
                    ARchiveApplication.sp.saveAccessToken(it.accessToken)
                    ARchiveApplication.sp.saveRefreshToken(it.refreshToken)
                }.onError {
                    // ToastMessage 있으면 좋을듯
                    Log.d("티티","submitSignInData 에러")
                }
            }
        }
    }

    override fun submitSignUpData(signUpData : SignUp){
        viewModelScope.launch {
            signUpUseCase(signUpData.toDto()).collect{result ->
                result.onSuccess {
                    // 토큰 저장 로직 추가
                    ARchiveApplication.sp.resetTokenData()
                    sharedPreferencesUtils.saveAccessToken(it.temporaryAccessToken)
                    signInEvent(AuthViewModel.SignInEvent.NavigateToSignUp)
                }.onError {
                    // ToastMessage 있으면 좋을듯
                    
                }
            }
        }
    }

    override fun getTemporaryToken(temporaryTokenReIssue : SignIn){
        viewModelScope.launch {
            temporaryTokenReIssueUseCase(temporaryTokenReIssue.toDto()).collect{ result ->
                result.onSuccess {
                    sharedPreferencesUtils.saveAccessToken(it.temporaryAccessToken)
                    signInEvent(AuthViewModel.SignInEvent.NavigateToSignUp)
                }.onFail {

                }.onError {

                }.onException {

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

    override fun setHash(hash : String) {
        appHash = hash
    }
    override fun checkPhoneNumber(): Boolean {
        val pattern = "^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$"
        if (!Pattern.matches(pattern, rawPhoneNumber.value)) {
            return false
        }
        return true
    }

    override fun submitPhoneNumber(){
        // 임시토큰 헤더에 넣고 해야함.
        viewModelScope.launch {
            sendMessageUseCase(VerificationMessageSend(rawPhoneNumber.value, appHash).toDto()).collect{ result ->
                result.onSuccess{
                    signUpEvent(AuthViewModel.SignUpEvent.NavigateToCertification)
                }.onFail {
                    // Toast 메시지 있으면 좋을듯
                    // 아마 5번 하루 5번 이상 이면 안 실패(?)
                    Log.d("티티","submitPhoneNumber 에러")

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

    override fun submitCertificationNumber(){
        viewModelScope.launch {
            validMessageUseCase(VerificationNumberValid(certificationNumber.value, rawPhoneNumber.value).toDto()).collect{ result ->
                result.onSuccess {
                    sharedPreferencesUtils.saveAccessToken(it.accessToken)
                    sharedPreferencesUtils.saveRefreshToken(it.refreshToken)
                    certificationEvent(AuthViewModel.CertificationEvent.NavigateToSignUpSuccess)

                }.onFail {
                    // Toast 메시지 있으면 좋을듯
                    resetCertificationNumber()
                    certificationEvent(AuthViewModel.CertificationEvent.failCertificationCode)
                    Log.d("티티","submitCertificationNumber 에러")

                }
            }
        }
    }

    override fun reSend(){
        initTimer()
        resetCertificationNumber()
        submitPhoneNumber()
    }

    private fun resetCertificationNumber(){
        certificationNumber1.value = ""
        certificationNumber2.value = ""
        certificationNumber3.value = ""
        certificationNumber4.value = ""
    }

}