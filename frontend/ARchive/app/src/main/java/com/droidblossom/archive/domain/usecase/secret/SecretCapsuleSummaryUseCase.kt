package com.droidblossom.archive.domain.usecase.secret

import android.util.Log
import com.droidblossom.archive.domain.repository.SecretRepository
import com.droidblossom.archive.util.onException
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SecretCapsuleSummaryUseCase @Inject constructor(
    private val repository : SecretRepository
) {

    suspend operator fun invoke(capsuleId :Long) =
        flow {
            try {
                emit(repository.getSecretCapsuleSummary(capsuleId).onSuccess {

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