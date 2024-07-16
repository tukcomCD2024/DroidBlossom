package com.droidblossom.archive.domain.usecase.group_capsule

import android.util.Log
import com.droidblossom.archive.data.dto.common.IdBasedPagingRequestDto
import com.droidblossom.archive.domain.repository.GroupCapsuleRepository
import com.droidblossom.archive.util.onException
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CapsulesOfGroupsPageUseCase @Inject constructor(
    private val repository: GroupCapsuleRepository
) {
    suspend operator fun invoke(request: IdBasedPagingRequestDto) =
        flow {
            try {
                emit(repository.getCapsulesOfGroupsPage(request = request).onSuccess {

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