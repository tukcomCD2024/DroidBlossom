package com.droidblossom.archive.domain.usecase

import com.droidblossom.archive.data.dto.auth.request.SignInRequestDto
import com.droidblossom.archive.domain.model.auth.Token
import com.droidblossom.archive.domain.repository.AuthRepository
import com.droidblossom.archive.util.RetrofitResult
import com.droidblossom.archive.util.onError
import com.droidblossom.archive.util.onException
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val repository: AuthRepository
) {

    suspend operator fun invoke(request: SignInRequestDto) =
        flow<RetrofitResult<Token>> {
            try {
                emit(repository.authSignIn(request).onSuccess {

                })
                emit(repository.authSignIn(request).onFail {

                })
                repository.authSignIn(request).onError {
                    throw Exception(it)
                }
                repository.authSignIn(request).onException {
                    throw Exception(it)
                }
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
}