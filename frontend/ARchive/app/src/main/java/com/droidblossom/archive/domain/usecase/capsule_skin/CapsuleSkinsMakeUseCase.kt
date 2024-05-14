package com.droidblossom.archive.domain.usecase.capsule_skin

import android.util.Log
import com.droidblossom.archive.data.dto.capsule_skin.request.CapsuleSkinsMakeRequestDto
import com.droidblossom.archive.domain.repository.CapsuleSkinRepository
import com.droidblossom.archive.util.onException
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CapsuleSkinsMakeUseCase @Inject constructor(
    private val repository: CapsuleSkinRepository
){

    suspend operator fun invoke(request: CapsuleSkinsMakeRequestDto) =
        flow{
            try {
                emit(repository.postCapsuleSkinMake(request).onSuccess {

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
