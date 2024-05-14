package com.droidblossom.archive.domain.usecase.secret

import android.util.Log
import com.droidblossom.archive.domain.model.common.CapsuleDetail
import com.droidblossom.archive.domain.repository.SecretRepository
import com.droidblossom.archive.util.RetrofitResult
import com.droidblossom.archive.util.onException
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SecretCapsuleDetailUseCase @Inject constructor(
    private val repository: SecretRepository
) {
    suspend operator fun invoke(capsuleId: Long) =
        flow<RetrofitResult<CapsuleDetail>> {
            try {
                emit(repository.getSecretCapsuleDetail(capsuleId).onSuccess {

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