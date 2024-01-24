package com.droidblossom.archive.domain.usecase

import com.droidblossom.archive.data.dto.auth.request.SignInRequestDto
import com.droidblossom.archive.domain.model.auth.TemporaryToken
import com.droidblossom.archive.domain.repository.AuthRepository
import com.droidblossom.archive.util.RetrofitResult
import com.droidblossom.archive.util.onError
import com.droidblossom.archive.util.onException
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TemporaryTokenReIssueUseCase @Inject constructor(
    private val repository : AuthRepository
) {
    suspend operator fun invoke(request: SignInRequestDto) =
        flow<RetrofitResult<TemporaryToken>> {
            emit(repository.authTemporaryTokenReIssue(request)
                .onSuccess {

                }.onFail {

                }.onError {

                }.onException {

                })

        }
}