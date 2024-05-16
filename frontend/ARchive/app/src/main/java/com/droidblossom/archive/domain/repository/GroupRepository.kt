package com.droidblossom.archive.domain.repository

import com.droidblossom.archive.data.dto.group.request.CreateGroupRequestDto
import com.droidblossom.archive.util.RetrofitResult

interface GroupRepository {
    suspend fun postGroupCreateRequest(request: CreateGroupRequestDto): RetrofitResult<String>

}