package com.droidblossom.archive.domain.usecase.auth

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

                }.onFail {

                }.onException {

                    throw Exception(it)
                })
            } catch (e: Exception) {
                Log.d("예외확인", "$e")
                e.printStackTrace()

            }
        }
}