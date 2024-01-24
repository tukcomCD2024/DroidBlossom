package com.droidblossom.archive.presentation.ui.auth

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.droidblossom.archive.domain.model.auth.SignIn
import com.droidblossom.archive.domain.model.auth.SignUp
import com.droidblossom.archive.domain.model.auth.VerificationMessageSend
import com.droidblossom.archive.domain.model.member.CheckStatus
import com.droidblossom.archive.domain.usecase.MemberStatusUseCase
import com.droidblossom.archive.domain.usecase.ReIssueUseCase
import com.droidblossom.archive.domain.usecase.SendMessageUseCase
import com.droidblossom.archive.domain.usecase.SignInUseCase
import com.droidblossom.archive.domain.usecase.SignUpUseCase
import com.droidblossom.archive.presentation.base.BaseViewModel
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
import kotlinx.coroutines.flow.collect
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
    private val validMessageUseCase: SendMessageUseCase,
    private val memberStatusUseCase: MemberStatusUseCase
) : BaseViewModel(), AuthViewModel {

    // SignInFragment
    private val _signInEvents = MutableSharedFlow<AuthViewModel.SignInEvent>()
    override val signInEvents: SharedFlow<AuthViewModel.SignInEvent> = _signInEvents.asSharedFlow()

    // SignUpFragment
    private val _signUpEvents = MutableSharedFlow<AuthViewModel.SigUpnEvent>()
    override val signUpEvents: SharedFlow<AuthViewModel.SigUpnEvent> = _signUpEvents.asSharedFlow()

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
                    Log.d("테스트", "$it")
                    if (it.isVerified){
                        submitSignInData(SignIn(memberStatusCheckData.authId, memberStatusCheckData.socialType))
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
                    signInEvent(AuthViewModel.SignInEvent.NavigateToMain)
                }.onError {
                    Log.d("테스트", "로그인 에러")

                }.onFail {
                    Log.d("테스트", "로그인 실패")

                }.onException {
                    Log.d("테스트", "로그인 예외")

                }
            }
        }
    }

    override fun submitSignUpData(signUpData : SignUp){
        Log.d("테스트", "회원가입")
        viewModelScope.launch {
            signUpUseCase(signUpData.toDto()).collect{result ->
                result.onSuccess {
                    // 토큰 저장 로직 추가
                    signInEvent(AuthViewModel.SignInEvent.NavigateToSignUp)
                }
            }
        }
    }

    // SignUpFragment
    override fun signUpEvent(event: AuthViewModel.SigUpnEvent) {
        viewModelScope.launch {
            _signUpEvents.emit(event)
        }
    }

    override fun setHash(hash : String) {
        appHash = hash
    }
    private fun checkPhoneNumber(): Boolean {
        val pattern = "^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$"
        if (!Pattern.matches(pattern, rawPhoneNumber.value)) {
            return false
        }

        return true
    }

    override fun submitPhoneNumber(){
        if (checkPhoneNumber()){
            viewModelScope.launch {
                sendMessageUseCase(VerificationMessageSend(rawPhoneNumber.value, appHash).toDto()).collect{ result ->
                    result.onSuccess {
                        Log.d("테스트", "폰 인증")
                        signUpEvent(AuthViewModel.SigUpnEvent.NavigateToCertification)
                    }.onError {
                        Log.d("테스트", "폰 인증 에러")
                    }.onFail {
                        Log.d("테스트", "폰 인증 실패")
                    }.onException {
                        Log.d("테스트", "폰 인증 예외")
                    }

                }
            }
        }else{
            // Toast 보여줘야할듯?
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

}