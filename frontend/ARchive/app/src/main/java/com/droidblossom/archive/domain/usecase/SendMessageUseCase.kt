package com.droidblossom.archive.domain.usecase

import android.util.Log
import com.droidblossom.archive.data.dto.auth.request.TokenReIssueRequestDto
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
import java.lang.Exception
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(request: VerificationMessageSendRequestDto) =
        flow<RetrofitResult<VerificationMessageResult>> {
            try {
                emit(repository.authValidMessageSend(request).onSuccess {
                    Log.d("티티","유스케이스 : submitPhoneNumber 에러")
                })
                emit(repository.authValidMessageSend(request).onFail {
                    Log.d("티티","유스케이스 : submitPhoneNumber 실패 $it")
                })
                repository.authValidMessageSend(request).onError {
                    Log.d("티티","유스케이스 : submitPhoneNumber 에러 : $it")
                    throw Exception(it)
                }
                repository.authValidMessageSend(request).onException {
                    Log.d("티티","유스케이스 : submitPhoneNumber 예외 :$it")
                    throw Exception(it)
                }
            } catch (e: Exception){
                e.printStackTrace()

            }
        }
}