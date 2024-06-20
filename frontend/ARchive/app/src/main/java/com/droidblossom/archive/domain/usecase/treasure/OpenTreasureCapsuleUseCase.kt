package com.droidblossom.archive.domain.usecase.treasure

import android.util.Log
import com.droidblossom.archive.domain.repository.TreasureRepository
import com.droidblossom.archive.util.onException
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class OpenTreasureCapsuleUseCase @Inject constructor(
    private val repository: TreasureRepository
){
    suspend operator fun invoke(capsuleId: Long)=
        flow {
            try {
                emit(repository.openTreasureCapsule(capsuleId).onSuccess {

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