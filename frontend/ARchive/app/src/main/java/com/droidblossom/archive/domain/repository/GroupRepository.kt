package com.droidblossom.archive.domain.repository

import com.droidblossom.archive.data.dto.common.PagingRequestDto
import com.droidblossom.archive.data.dto.group.request.CreateGroupRequestDto
import com.droidblossom.archive.domain.model.group.GroupPage
import com.droidblossom.archive.util.RetrofitResult

interface GroupRepository {
    suspend fun postGroupCreateRequest(request: CreateGroupRequestDto): RetrofitResult<String>

    suspend fun getGroupPageRequest(request: PagingRequestDto): RetrofitResult<GroupPage>

    suspend fun postAcceptGroupInviteRequest(groupId: Long, targetId: Long): RetrofitResult<String>

}