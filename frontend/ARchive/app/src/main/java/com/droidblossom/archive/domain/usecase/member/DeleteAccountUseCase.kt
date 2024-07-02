package com.droidblossom.archive.domain.usecase.member

import android.util.Log
import com.droidblossom.archive.data.dto.member.request.FcmTokenRequsetDto
import com.droidblossom.archive.domain.repository.MemberRepository
import com.droidblossom.archive.util.RetrofitResult
import com.droidblossom.archive.util.onException
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteAccountUseCase @Inject constructor(
    private val repository: MemberRepository
) {
    suspend operator fun invoke() =
        flow<RetrofitResult<String>> {
            try {
                emit(repository.deleteAccount()
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