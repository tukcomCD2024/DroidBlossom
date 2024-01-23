package com.droidblossom.archive.domain.usecase

import android.util.Log
import com.droidblossom.archive.data.dto.auth.request.SignInRequestDto
import com.droidblossom.archive.data.dto.auth.request.VerificationMessageSendRequestDto
import com.droidblossom.archive.domain.model.auth.Token
import com.droidblossom.archive.domain.model.auth.VerificationMessageResult
import com.droidblossom.archive.domain.repository.AuthRepository
import com.droidblossom.archive.util.RetrofitResult
import com.droidblossom.archive.util.onError
import com.droidblossom.archive.util.onException
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val repository : AuthRepository
) {

    suspend operator fun invoke(request : SignInRequestDto) = flow<RetrofitResult<Token>>{
        repository.authSignIn(request)
            .onSuccess {
                Log.d("후후후", "성공 - 로그인")

            }.onFail {
                Log.d("후후후", "실패 - 로그인")

            }.onError {
                Log.d("후후후", "에러 - 로그인")

            }.onException {
                Log.d("후후후", "예외 - 로그인")

            }
    }
}