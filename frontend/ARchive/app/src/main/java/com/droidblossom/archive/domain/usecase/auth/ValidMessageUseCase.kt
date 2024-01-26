package com.droidblossom.archive.domain.usecase.auth

import android.util.Log
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
    private val repository: AuthRepository
) {
    suspend operator fun invoke(request: VerificationNumberValidRequestDto) =
        flow<RetrofitResult<Token>> {
            try {
                emit(repository.authValidMessage(request).onSuccess {
                    Log.d("성패", "$it")
                }.onFail {
                    Log.d("실패", "$it")
                }.onException {
                    Log.d("실패", "$it")
                    throw Exception(it)
                })
            } catch (e: Exception) {
                Log.d("예외확인", "$e")
                e.printStackTrace()
            }
        }
}