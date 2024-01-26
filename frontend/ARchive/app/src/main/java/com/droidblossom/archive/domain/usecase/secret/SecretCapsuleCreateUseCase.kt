package com.droidblossom.archive.domain.usecase.secret

import android.util.Log
import com.droidblossom.archive.data.dto.secret.request.SecretCapsuleCreateRequestDto
import com.droidblossom.archive.domain.model.secret.SecretCapsulePage
import com.droidblossom.archive.domain.repository.SecretRepository
import com.droidblossom.archive.util.RetrofitResult
import com.droidblossom.archive.util.onException
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SecretCapsuleCreateUseCase @Inject constructor(
    private val repository: SecretRepository
) {
    suspend operator fun invoke(request: SecretCapsuleCreateRequestDto) =
        flow<RetrofitResult<SecretCapsulePage>> {
            try {
                emit(repository.createSecretCapsule(request).onSuccess {

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