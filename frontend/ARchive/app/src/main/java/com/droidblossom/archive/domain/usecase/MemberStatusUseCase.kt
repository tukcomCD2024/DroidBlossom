package com.droidblossom.archive.domain.usecase

import android.util.Log
import com.droidblossom.archive.data.dto.auth.request.VerificationMessageSendRequestDto
import com.droidblossom.archive.data.dto.member.request.MemberStatusRequestDto
import com.droidblossom.archive.data.dto.member.response.MemberStatusResponseDto
import com.droidblossom.archive.domain.model.auth.VerificationMessageResult
import com.droidblossom.archive.domain.model.member.MemberStatus
import com.droidblossom.archive.domain.repository.MemberRepository
import com.droidblossom.archive.util.RetrofitResult
import com.droidblossom.archive.util.onError
import com.droidblossom.archive.util.onException
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MemberStatusUseCase @Inject constructor(
    private val repository: MemberRepository
) {
    suspend operator fun invoke(request: MemberStatusRequestDto) =
        flow<RetrofitResult<MemberStatus>> {
            emit(repository.postMemberStatus(request)
                .onSuccess {
                    Log.d("후후후", "성공 - 멤버 상태")

                }.onFail {
                    Log.d("후후후", "실패 - 멤버 상태")

                }.onError {
                    Log.d("후후후", "에러 - 멤버 상태")

                }.onException {
                    Log.d("후후후", "예외 - 멤버 상태")

                }
            )
        }
}