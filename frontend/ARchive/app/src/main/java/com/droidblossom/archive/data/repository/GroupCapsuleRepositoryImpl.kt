package com.droidblossom.archive.data.repository

import com.droidblossom.archive.data.dto.ResponseBody
import com.droidblossom.archive.data.dto.common.CapsuleCreateRequestDto
import com.droidblossom.archive.data.dto.common.toModel
import com.droidblossom.archive.data.source.remote.api.GroupCapsuleService
import com.droidblossom.archive.domain.repository.GroupCapsuleRepository
import com.droidblossom.archive.util.RetrofitResult
import com.droidblossom.archive.util.apiHandler
import javax.inject.Inject

class GroupCapsuleRepositoryImpl @Inject constructor(
    private val api: GroupCapsuleService
) : GroupCapsuleRepository {

    override suspend fun createGroupCapsuleCapsule(
        groupId: Long,
        request: CapsuleCreateRequestDto
    ): RetrofitResult<String> {
        return apiHandler({
            api.postGroupCapsuleApi(
                groupId,
                request
            )
        }) { response: ResponseBody<String> -> response.result.toModel() }
    }

}