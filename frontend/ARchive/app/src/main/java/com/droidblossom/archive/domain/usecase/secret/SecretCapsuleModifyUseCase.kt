package com.droidblossom.archive.domain.usecase.secret

import android.util.Log
import com.droidblossom.archive.data.dto.secret.request.SecretCapsuleModifyRequestDto
import com.droidblossom.archive.domain.model.secret.SecretCapsuleModify
import com.droidblossom.archive.domain.repository.SecretRepository
import com.droidblossom.archive.util.RetrofitResult
import com.droidblossom.archive.util.onException
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

data class SecretCapsuleModifyUseCase @Inject constructor(
    private val repository: SecretRepository
) {
    suspend operator fun invoke(capsuleId :Int,request: SecretCapsuleModifyRequestDto) =
        flow<RetrofitResult<SecretCapsuleModify>> {
            try {
                emit(repository.modifySecretCapsule(capsuleId, request).onSuccess {

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
