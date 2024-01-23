package com.droidblossom.archive.domain.usecase

import android.util.Log
import com.droidblossom.archive.data.dto.auth.request.SignUpRequestDto
import com.droidblossom.archive.domain.model.auth.TemporaryToken
import com.droidblossom.archive.domain.repository.AuthRepository
import com.droidblossom.archive.util.RetrofitResult
import com.droidblossom.archive.util.onError
import com.droidblossom.archive.util.onException
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(request: SignUpRequestDto) = flow<RetrofitResult<TemporaryToken>> {
        repository.authSignUp(request)
            .onSuccess {
                Log.d("후후후", "성공 - 회원가입")

            }.onFail {
                Log.d("후후후", "실패 - 회원가입")

            }.onError {
                Log.d("후후후", "에러 - 회원가입")

            }.onException {
                Log.d("후후후", "예외 - 회원가입")

            }
    }
}