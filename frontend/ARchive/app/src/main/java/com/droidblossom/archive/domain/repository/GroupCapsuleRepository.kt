package com.droidblossom.archive.domain.repository

import com.droidblossom.archive.data.dto.common.CapsuleCreateRequestDto
import com.droidblossom.archive.data.dto.common.PagingRequestDto
import com.droidblossom.archive.domain.model.group_capsule.GroupCapsuleOpenStateResponse
import com.droidblossom.archive.domain.model.group_capsule.GroupCapsuleSummaryResponse
import com.droidblossom.archive.domain.model.group_capsule.GroupMembersCapsuleOpenStatusResponse
import com.droidblossom.archive.domain.model.secret.CapsulePageList
import com.droidblossom.archive.util.RetrofitResult

interface GroupCapsuleRepository {

    suspend fun createGroupCapsuleCapsule(
        groupId: Long,
        request: CapsuleCreateRequestDto
    ): RetrofitResult<String>

    suspend fun openGroupCapsule(
        groupId: Long
    ): RetrofitResult<GroupCapsuleOpenStateResponse>

    suspend fun getGroupCapsuleSummary(
        capsuleId: Long
    ): RetrofitResult<GroupCapsuleSummaryResponse>

    suspend fun getMyGroupCapsulesPage(
        request: PagingRequestDto
    ) : RetrofitResult<CapsulePageList>

    suspend fun getGroupCapsulesMemberOpenStatus(
        capsuleId: Long,
        groupId: Long
    ): RetrofitResult<GroupMembersCapsuleOpenStatusResponse>
}