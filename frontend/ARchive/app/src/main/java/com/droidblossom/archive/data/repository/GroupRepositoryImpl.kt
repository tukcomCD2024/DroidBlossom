package com.droidblossom.archive.data.repository

import com.droidblossom.archive.data.dto.ResponseBody
import com.droidblossom.archive.data.dto.common.IdBasedPagingRequestDto
import com.droidblossom.archive.data.dto.common.PagingRequestDto
import com.droidblossom.archive.data.dto.common.toModel
import com.droidblossom.archive.data.dto.group.request.CreateGroupRequestDto
import com.droidblossom.archive.data.dto.group.request.InviteGroupRequestDto
import com.droidblossom.archive.data.dto.group.response.GroupDetailResponseDto
import com.droidblossom.archive.data.dto.group.response.GroupInvitedUsersPageResponseDto
import com.droidblossom.archive.data.dto.group.response.GroupInvitesPageResponseDto
import com.droidblossom.archive.data.dto.group.response.GroupMembersInfoResponseDto
import com.droidblossom.archive.data.dto.group.response.GroupPageResponseDto
import com.droidblossom.archive.data.source.remote.api.GroupService
import com.droidblossom.archive.domain.model.group.GroupDetail
import com.droidblossom.archive.domain.model.group.GroupInviteSummary
import com.droidblossom.archive.domain.model.group.GroupInvitedUsersPage
import com.droidblossom.archive.domain.model.group.GroupMembersInfo
import com.droidblossom.archive.domain.model.group.GroupPage
import com.droidblossom.archive.domain.model.group.GroupSummary
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

    override suspend fun getGroupPageRequest(request: PagingRequestDto): RetrofitResult<GroupPage<GroupSummary>> {
        return apiHandler({
            api.getGroupsPageApi(
                request.size,
                request.createdAt
            )
        }) { response: ResponseBody<GroupPageResponseDto> -> response.result.toModel() }

    }

    override suspend fun getGroupInvitesPageRequest(request: PagingRequestDto): RetrofitResult<GroupPage<GroupInviteSummary>> {
        return apiHandler({
            api.getGroupInvitesPageApi(
                request.size,
                request.createdAt
            )
        }) { response: ResponseBody<GroupInvitesPageResponseDto> -> response.result.toModel() }

    }

    override suspend fun postAcceptGroupInviteRequest(
        groupId: Long
    ): RetrofitResult<String> {
        return apiHandler({
            api.postAcceptGroupInviteApi(
                groupId,
            )
        }) { response: ResponseBody<String> -> response.result.toModel() }
    }

    override suspend fun deleteRejectGroupInviteRequest(
        groupId: Long,
        targetId: Long
    ): RetrofitResult<String> {
        return apiHandler({
            api.deleteRejectGroupInviteApi(
                groupId,
                targetId
            )
        }) { response: ResponseBody<String> -> response.result.toModel() }
    }

    override suspend fun getGroupDetailRequest(groupId: Long): RetrofitResult<GroupDetail> {
        return apiHandler({ api.getGroupDetailApi(groupId) }) { response: ResponseBody<GroupDetailResponseDto> -> response.result.toModel() }
    }

    override suspend fun postGroupInviteRequest(request: InviteGroupRequestDto): RetrofitResult<String> {
        return apiHandler({ api.postGroupsInviteApi(request) }) { response: ResponseBody<String> -> response.result.toString() }
    }

    override suspend fun getGroupMemberRequest(groupId: Long): RetrofitResult<GroupMembersInfo> {
        return apiHandler({ api.getGroupMembersApi(groupId) }) { response: ResponseBody<GroupMembersInfoResponseDto> -> response.result.toModel() }
    }

    override suspend fun getGroupInvitedUsersRequest(groupId: Long,pagingRequest: IdBasedPagingRequestDto): RetrofitResult<GroupInvitedUsersPage> {
        return apiHandler({ api.getGroupInvitedUsersApi(groupId = groupId, size = pagingRequest.size, pagingId = pagingRequest.pagingId) }) { response: ResponseBody<GroupInvitedUsersPageResponseDto> -> response.result.toModel() }
    }
    override suspend fun deleteLeaveGroupRequest(groupId: Long): RetrofitResult<String> {
        return apiHandler({ api.deleteLeaveGroupApi(groupId) }) { response: ResponseBody<String> -> response.result.toString() }
    }

}