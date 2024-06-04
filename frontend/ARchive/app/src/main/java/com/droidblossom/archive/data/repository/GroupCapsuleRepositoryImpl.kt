package com.droidblossom.archive.data.repository

import com.droidblossom.archive.data.dto.ResponseBody
import com.droidblossom.archive.data.dto.common.CapsuleCreateRequestDto
import com.droidblossom.archive.data.dto.common.toModel
import com.droidblossom.archive.data.dto.group_capsule.GroupCapsuleSummaryResponseDto
import com.droidblossom.archive.data.source.remote.api.GroupCapsuleService
import com.droidblossom.archive.domain.model.group_capsule.GroupCapsuleSummaryResponse
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

    override suspend fun openGroupCapsule(groupId: Long): RetrofitResult<String> {
        return  apiHandler({ api.postGroupCapsuleOpenApi(groupId) }) { response: ResponseBody<String> -> response.result.toModel() }
    }

    override suspend fun getGroupCapsuleSummary(capsuleId: Long): RetrofitResult<GroupCapsuleSummaryResponse> {
        return  apiHandler({ api.getGroupCapsuleSummaryApi(capsuleId) }) { response: ResponseBody<GroupCapsuleSummaryResponseDto> -> response.result.toModel() }
    }

}