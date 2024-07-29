package com.droidblossom.archive.domain.usecase.capsule_skin

import android.util.Log
import com.droidblossom.archive.domain.model.capsule_skin.CapsuleSkinDeleteResultResponse
import com.droidblossom.archive.domain.repository.CapsuleSkinRepository
import com.droidblossom.archive.util.RetrofitResult
import com.droidblossom.archive.util.onException
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CapsuleSkinDeleteUseCase @Inject constructor(
    private val repository: CapsuleSkinRepository
){

    suspend operator fun invoke(capsuleSkinId :Long) =
        flow<RetrofitResult<CapsuleSkinDeleteResultResponse>> {
            try {
                emit(repository.deleteCapsuleSkin(capsuleSkinId).onSuccess {

                }.onFail {

                }.onException {
                    throw Exception(it)
                })
            }catch (e : Exception){
                Log.d("예외확인", "$e")
                e.printStackTrace()
            }
        }
}