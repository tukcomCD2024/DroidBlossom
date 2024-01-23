package com.droidblossom.archive.domain.usecase

import android.util.Log
import com.droidblossom.archive.data.dto.auth.request.TokenReIssueRequestDto
import com.droidblossom.archive.data.dto.auth.request.VerificationNumberValidRequestDto
import com.droidblossom.archive.domain.model.auth.Token
import com.droidblossom.archive.domain.repository.AuthRepository
import com.droidblossom.archive.util.RetrofitResult
import com.droidblossom.archive.util.onError
import com.droidblossom.archive.util.onException
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ValidMessageUseCase @Inject constructor(
    private val repository : AuthRepository
) {
    suspend operator fun invoke(request : VerificationNumberValidRequestDto) = flow<RetrofitResult<Token>>{
        repository.authValidMessage(request)
            .onSuccess {
                Log.d("후후후", "성공 - 인증번호 확인")

            }.onFail {
                Log.d("후후후", "실패 - 인증번호 확인")

            }.onError {
                Log.d("후후후", "에러 - 인증번호 확인")

            }.onException {
                Log.d("후후후", "예외 - 인증번호 확인")

            }
    }
}