package com.droidblossom.archive.domain.usecase.group_capsule

import android.util.Log
import com.droidblossom.archive.domain.model.group_capsule.GroupCapsuleSummaryResponse
import com.droidblossom.archive.domain.repository.GroupCapsuleRepository
import com.droidblossom.archive.util.RetrofitResult
import com.droidblossom.archive.util.onException
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GroupCapsuleSummaryUseCase @Inject constructor(
    private val repository: GroupCapsuleRepository
) {
    suspend operator fun invoke(capsuleId: Long) =
        flow<RetrofitResult<GroupCapsuleSummaryResponse>> {
            try {
                emit(repository.getGroupCapsuleSummary(capsuleId).onSuccess {

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