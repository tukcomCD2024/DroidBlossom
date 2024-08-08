package com.droidblossom.archive.data.repository

import com.droidblossom.archive.data.dto.ResponseBody
import com.droidblossom.archive.data.dto.common.CapsuleCreateRequestDto
import com.droidblossom.archive.data.dto.common.IdBasedPagingRequestDto
import com.droidblossom.archive.data.dto.common.PagingRequestDto
import com.droidblossom.archive.data.dto.common.toModel
import com.droidblossom.archive.data.dto.group.response.MyGroupCapsulePageResponseDto
import com.droidblossom.archive.data.dto.group_capsule.CapsulesOfGroupResponseDto
import com.droidblossom.archive.data.dto.group_capsule.GroupCapsuleDetailResponseDto
import com.droidblossom.archive.data.dto.group_capsule.GroupCapsuleOpenStateResponseDto
import com.droidblossom.archive.data.dto.group_capsule.GroupCapsuleSliceResponseDto
import com.droidblossom.archive.data.dto.group_capsule.GroupCapsuleSummaryResponseDto
import com.droidblossom.archive.data.dto.group_capsule.GroupMembersCapsuleOpenStatusResponseDto
import com.droidblossom.archive.data.source.remote.api.GroupCapsuleService
import com.droidblossom.archive.domain.model.common.CapsuleDetail
import com.droidblossom.archive.domain.model.group_capsule.GroupCapsuleOpenStateResponse
import com.droidblossom.archive.domain.model.group_capsule.GroupCapsuleSliceResponse
import com.droidblossom.archive.domain.model.group_capsule.GroupCapsuleSummaryResponse
import com.droidblossom.archive.domain.model.group_capsule.GroupMembersCapsuleOpenStatusResponse
import com.droidblossom.archive.domain.model.secret.CapsulePageList
import com.droidblossom.archive.domain.repository.GroupCapsuleRepository
import com.droidblossom.archive.util.RetrofitResult
import com.droidblossom.archive.util.apiHandler
import javax.inject.Inject

class GroupCapsuleRepositoryImpl @Inject constructor(
    private val groupCapsuleService: GroupCapsuleService
) : GroupCapsuleRepository {
    override suspend fun getCapsulesOfGroupsPage(request: IdBasedPagingRequestDto): RetrofitResult<GroupCapsuleSliceResponse> {
        return apiHandler({
            groupCapsuleService.getCapsulesOfGroupsPageApi(
                size = request.size,
                pagingId = request.pagingId
            )
        }) { response: ResponseBody<GroupCapsuleSliceResponseDto> -> response.result.toModel()}
    }

    override suspend fun createGroupCapsuleCapsule(
        groupId: Long,
        request: CapsuleCreateRequestDto
    ): RetrofitResult<String> {
        return apiHandler({
            groupCapsuleService.postGroupCapsuleApi(
                groupId,
                request
            )
        }) { response: ResponseBody<String> -> response.result.toModel() }
    }

    override suspend fun openGroupCapsule(groupId: Long): RetrofitResult<GroupCapsuleOpenStateResponse> {
        return  apiHandler({ groupCapsuleService.postGroupCapsuleOpenApi(groupId) }) { response: ResponseBody<GroupCapsuleOpenStateResponseDto> -> response.result.toModel()}
    }

    override suspend fun getGroupCapsuleSummary(capsuleId: Long): RetrofitResult<GroupCapsuleSummaryResponse> {
        return  apiHandler({ groupCapsuleService.getGroupCapsuleSummaryApi(capsuleId) }) { response: ResponseBody<GroupCapsuleSummaryResponseDto> -> response.result.toModel() }
    }

    override suspend fun getGroupCapsuleDetail(capsuleId: Long): RetrofitResult<CapsuleDetail> {
        return  apiHandler({ groupCapsuleService.getGroupCapsuleDetailApi(capsuleId) }) { response: ResponseBody<GroupCapsuleDetailResponseDto> -> response.result.toModel() }
    }

    override suspend fun getMyGroupCapsulesPage(request: PagingRequestDto): RetrofitResult<CapsulePageList> {
        return apiHandler({ groupCapsuleService.getMyGroupCapsulesApi(request.size, request.createdAt) }){ response: ResponseBody<MyGroupCapsulePageResponseDto> -> response.result.toModel() }
    }

    override suspend fun getCapsulesPageOfGroup(
        groupId: Long,
        request: IdBasedPagingRequestDto
    ): RetrofitResult<CapsulePageList> {
        return apiHandler({ groupCapsuleService.getCapsulesOfGroupApi(groupId = groupId, size = request.size, pagingId = request.pagingId) }){ response: ResponseBody<CapsulesOfGroupResponseDto> -> response.result.toModel() }

    }

    override suspend fun getGroupCapsulesMemberOpenStatus(
        capsuleId: Long,
        groupId: Long
    ): RetrofitResult<GroupMembersCapsuleOpenStatusResponse> {
        return apiHandler({ groupCapsuleService.getGroupCapsulesMemberOpenStatusApi(capsuleId = capsuleId, groupId = groupId) }){ response: ResponseBody<GroupMembersCapsuleOpenStatusResponseDto> -> response.result.toModel() }
    }


}