package com.droidblossom.archive.domain.usecase.capsule

import android.util.Log
import com.droidblossom.archive.domain.repository.CapsuleRepository
import com.droidblossom.archive.util.onException
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteCapsuleUseCase @Inject constructor(
    private val repository: CapsuleRepository
) {

    suspend operator fun invoke(capsuleId: Long, capsuleType: String) =
        flow {
            try {
                emit(repository.deleteCapsule(capsuleId, capsuleType)
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