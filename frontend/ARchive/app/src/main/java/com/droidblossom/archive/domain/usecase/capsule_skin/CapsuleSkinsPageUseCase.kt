package com.droidblossom.archive.domain.usecase.capsule_skin

import android.util.Log
import com.droidblossom.archive.data.dto.common.PagingRequestDto
import com.droidblossom.archive.domain.repository.CapsuleSkinRepository
import com.droidblossom.archive.util.onException
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CapsuleSkinsPageUseCase @Inject constructor(
    private val repository: CapsuleSkinRepository
){

    suspend operator fun invoke(request: PagingRequestDto) =
        flow{
            try {
                emit(repository.getCapsuleSkinPage(request).onSuccess {

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