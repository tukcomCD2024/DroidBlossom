package com.droidblossom.archive.domain.usecase.capsule

import android.util.Log
import com.droidblossom.archive.domain.model.capsule.CapsuleImages
import com.droidblossom.archive.domain.repository.CapsuleRepository
import com.droidblossom.archive.util.RetrofitResult
import com.droidblossom.archive.util.onException
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PatchCapsuleOpenedUseCase @Inject constructor(
    private val repository: CapsuleRepository
) {
    suspend operator fun invoke(capsuleId: Long) =
        flow<RetrofitResult<String>> {
            try {
                emit(repository.openCapsule(capsuleId)
                    .onSuccess {

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