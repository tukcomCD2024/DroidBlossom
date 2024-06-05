package com.droidblossom.archive.domain.repository

import com.droidblossom.archive.data.dto.common.PagingRequestDto
import com.droidblossom.archive.data.dto.group.request.CreateGroupRequestDto
import com.droidblossom.archive.data.dto.group.request.InviteGroupRequestDto
import com.droidblossom.archive.domain.model.group.GroupDetail
import com.droidblossom.archive.domain.model.group.GroupInviteSummary
import com.droidblossom.archive.domain.model.group.GroupInvitedUsers
import com.droidblossom.archive.domain.model.group.GroupMembersInfo
import com.droidblossom.archive.domain.model.group.GroupPage
import com.droidblossom.archive.domain.model.group.GroupSummary
import com.droidblossom.archive.util.RetrofitResult

interface GroupRepository {
    suspend fun postGroupCreateRequest(request: CreateGroupRequestDto): RetrofitResult<String>

    suspend fun getGroupPageRequest(request: PagingRequestDto): RetrofitResult<GroupPage<GroupSummary>>

    suspend fun getGroupInvitesPageRequest(request: PagingRequestDto): RetrofitResult<GroupPage<GroupInviteSummary>>

    suspend fun postAcceptGroupInviteRequest(groupId: Long): RetrofitResult<String>

    suspend fun deleteRejectGroupInviteRequest(groupId: Long, targetId: Long): RetrofitResult<String>

    suspend fun getGroupDetailRequest(groupId: Long): RetrofitResult<GroupDetail>

    suspend fun postGroupInviteRequest(request: InviteGroupRequestDto): RetrofitResult<String>

    suspend fun getGroupMemberRequest(groupId: Long): RetrofitResult<GroupMembersInfo>

    suspend fun getGroupInvitedUsersRequest(groupId: Long): RetrofitResult<GroupInvitedUsers>

    suspend fun deleteLeaveGroupRequest(groupId: Long): RetrofitResult<String>
}