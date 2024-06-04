package com.droidblossom.archive.domain.repository

import com.droidblossom.archive.data.dto.common.CapsuleCreateRequestDto
import com.droidblossom.archive.domain.model.group_capsule.GroupCapsuleSummaryResponse
import com.droidblossom.archive.util.RetrofitResult

interface GroupCapsuleRepository {

    suspend fun createGroupCapsuleCapsule(
        groupId: Long,
        request: CapsuleCreateRequestDto
    ): RetrofitResult<String>

    suspend fun openGroupCapsule(
        groupId: Long
    ): RetrofitResult<String>

    suspend fun getGroupCapsuleSummary(
        capsuleId: Long
    ): RetrofitResult<GroupCapsuleSummaryResponse>
}