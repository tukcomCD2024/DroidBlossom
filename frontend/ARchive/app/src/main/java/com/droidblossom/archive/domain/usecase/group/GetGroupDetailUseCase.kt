package com.droidblossom.archive.domain.usecase.group

import android.util.Log
import com.droidblossom.archive.domain.repository.GroupRepository
import com.droidblossom.archive.util.onException
import com.droidblossom.archive.util.onFail
import com.droidblossom.archive.util.onSuccess
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetGroupDetailUseCase @Inject constructor(
    private val repository: GroupRepository
) {
    suspend operator fun invoke(groupId:Long) =
        flow{
            try {
                emit(
                    repository.getGroupDetailRequest(groupId = groupId)
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