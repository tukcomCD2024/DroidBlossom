package com.droidblossom.archive.domain.usecase.secret

import android.util.Log
import com.droidblossom.archive.data.dto.common.PagingRequestDto
import com.droidblossom.archive.domain.model.secret.CapsulePageList
import com.droidblossom.archive.domain.repository.SecretRepository
import com.droidblossom.archive.util.RetrofitResult
import com.droidblossom.archive.util.onException
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SecretCapsulePageUseCase @Inject constructor(
    private val repository: SecretRepository
) {
    suspend operator fun invoke(request: PagingRequestDto) =
        flow<RetrofitResult<CapsulePageList>> {
            try {
                emit(repository.getSecretCapsulePage(request).onSuccess {

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