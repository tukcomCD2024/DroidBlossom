package com.droidblossom.archive.domain.usecase

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
    suspend operator fun invoke(request: SignUpRequestDto) =
        flow<RetrofitResult<TemporaryToken>> {
            try {
                emit(repository.authSignUp(request).onSuccess {

                })
                emit(repository.authSignUp(request).onFail {

                })
                repository.authSignUp(request).onError {
                    throw Exception(it)
                }
                repository.authSignUp(request).onException {
                    throw Exception(it)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
}