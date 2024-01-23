package com.droidblossom.archive.domain.usecase

import android.util.Log
import com.droidblossom.archive.domain.model.auth.Health
import com.droidblossom.archive.domain.repository.MemberRepository
import com.droidblossom.archive.util.RetrofitResult
import com.droidblossom.archive.util.onError
import com.droidblossom.archive.util.onException
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TestUseCase @Inject constructor(
    private val repository: MemberRepository
) {
    operator fun invoke() = flow<RetrofitResult<Health>> {

        emit(repository.getText().onSuccess {
            Log.d("qwer", "${it.message}")

        }.onFail {
            Log.d("qwer", "실패")

        }.onError {
            Log.d("qwer", "에러")

        }.onException {
            Log.d("qwer", "예외")

        })
    }
}