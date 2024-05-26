package com.droidblossom.archive.domain.repository

import com.droidblossom.archive.data.dto.group_capsule.GroupCapsuleCreateRequestDto
import com.droidblossom.archive.util.RetrofitResult

interface GroupCapsuleRepository {

    suspend fun createGroupCapsuleCapsule(
        groupId: Long,
        request: GroupCapsuleCreateRequestDto
    ): RetrofitResult<String>
}