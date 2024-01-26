package com.droidblossom.archive.domain.usecase.member

import android.util.Log
import com.droidblossom.archive.domain.model.member.MemberDetail
import com.droidblossom.archive.domain.repository.MemberRepository
import com.droidblossom.archive.util.RetrofitResult
import com.droidblossom.archive.util.onError
import com.droidblossom.archive.util.onException
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MemberUseCase @Inject constructor(
    private val repository: MemberRepository
) {
    operator fun invoke() = flow<RetrofitResult<MemberDetail>> {
        try {
            emit(repository.getMe().onSuccess {
                Log.d("qwer", "성공")
            }.onFail {
                Log.d("qwer", "${it}")
            }.onError {
                throw Exception(it)
            }.onException {
                throw Exception(it)
            })
        } catch (e: Exception) {
            Log.d("예외확인", "$e")
            e.printStackTrace()
        }
    }
}