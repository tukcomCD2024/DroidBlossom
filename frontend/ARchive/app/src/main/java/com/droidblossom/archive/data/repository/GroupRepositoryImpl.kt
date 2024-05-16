package com.droidblossom.archive.data.repository

import com.droidblossom.archive.data.dto.ResponseBody
import com.droidblossom.archive.data.dto.common.PagingRequestDto
import com.droidblossom.archive.data.dto.common.toModel
import com.droidblossom.archive.data.dto.group.request.CreateGroupRequestDto
import com.droidblossom.archive.data.dto.group.response.GroupPageResponseDto
import com.droidblossom.archive.data.source.remote.api.GroupService
import com.droidblossom.archive.domain.model.group.GroupPage
import com.droidblossom.archive.domain.repository.GroupRepository
import com.droidblossom.archive.util.RetrofitResult
import com.droidblossom.archive.util.apiHandler
import javax.inject.Inject

class GroupRepositoryImpl @Inject constructor(
    private val api: GroupService
) : GroupRepository {
    override suspend fun postGroupCreateRequest(request: CreateGroupRequestDto): RetrofitResult<String> {
        return apiHandler({ api.postCrateGroupApi(request) }) { response: ResponseBody<String> -> response.result.toModel() }
    }

    override suspend fun getGroupPageRequest(request: PagingRequestDto): RetrofitResult<GroupPage> {
        return apiHandler({
            api.getGroupsPageApi(
                request.size,
                request.createdAt
            )
        }) { response: ResponseBody<GroupPageResponseDto> -> response.result.toModel() }

    }


}