package com.droidblossom.archive.domain.usecase.member

import android.util.Log
import com.droidblossom.archive.data.dto.auth.request.VerificationNumberValidRequestDto
import com.droidblossom.archive.domain.model.auth.Token
import com.droidblossom.archive.domain.repository.AuthRepository
import com.droidblossom.archive.domain.repository.MemberRepository
import com.droidblossom.archive.util.RetrofitResult
import com.droidblossom.archive.util.onError
import com.droidblossom.archive.util.onException
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChangePhoneValidMessageUseCase @Inject constructor(
    private val repository: MemberRepository
) {
    suspend operator fun invoke(request: VerificationNumberValidRequestDto) =
        flow<RetrofitResult<String>> {
            try {
                emit(repository.changePhoneValidMessage(request).onSuccess {

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