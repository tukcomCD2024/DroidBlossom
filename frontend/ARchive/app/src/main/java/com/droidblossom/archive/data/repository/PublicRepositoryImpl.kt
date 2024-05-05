package com.droidblossom.archive.data.repository

import com.droidblossom.archive.data.dto.ResponseBody
import com.droidblossom.archive.data.dto.common.CapsuleCreateRequestDto
import com.droidblossom.archive.data.dto.common.CapsuleDetailResponseDto
import com.droidblossom.archive.data.dto.common.CapsuleSummaryResponseDto
import com.droidblossom.archive.data.dto.common.PagingRequestDto
import com.droidblossom.archive.data.dto.common.toModel
import com.droidblossom.archive.data.dto.open.response.MyPublicCapsulePageResponseDto
import com.droidblossom.archive.data.dto.open.response.PublicCapsuleSliceResponseDto
import com.droidblossom.archive.data.source.remote.api.PublicService
import com.droidblossom.archive.domain.model.common.CapsuleDetail
import com.droidblossom.archive.domain.model.common.CapsuleSummaryResponse
import com.droidblossom.archive.domain.model.open.PublicCapsuleSliceResponse
import com.droidblossom.archive.domain.model.secret.CapsulePageList
import com.droidblossom.archive.domain.repository.PublicRepository
import com.droidblossom.archive.util.RetrofitResult
import com.droidblossom.archive.util.apiHandler
import javax.inject.Inject

class PublicRepositoryImpl @Inject constructor(
    private val api: PublicService
): PublicRepository {
    override suspend fun createPublicCapsule(request: CapsuleCreateRequestDto): RetrofitResult<String> {
        return apiHandler({ api.postPublicCapsuleApi(request) }) { response: ResponseBody<String> -> response.result.toModel() }
    }

    override suspend fun getPublicCapsuleSummary(capsuleId: Int): RetrofitResult<CapsuleSummaryResponse> {
        return apiHandler({ api.getPublicCapsuleSummaryApi(capsuleId) }) { response: ResponseBody<CapsuleSummaryResponseDto> -> response.result.toModel()}
    }

    override suspend fun getPublicCapsuleDetail(capsuleId: Long): RetrofitResult<CapsuleDetail> {
        return apiHandler({ api.getPublicCapsuleDetailApi(capsuleId) }) { response: ResponseBody<CapsuleDetailResponseDto> -> response.result.toModel()}
    }

    override suspend fun getPublicCapsulesPage(request: PagingRequestDto): RetrofitResult<PublicCapsuleSliceResponse> {
        return apiHandler({ api.getPublicCapsulesPageApi(request.size, request.createdAt) }){ response: ResponseBody<PublicCapsuleSliceResponseDto> -> response.result.toModel() }
    }

    override suspend fun getMyPublicCapsulesPage(request: PagingRequestDto): RetrofitResult<CapsulePageList> {
        return apiHandler({ api.getMyPublicCapsulePageApi(request.size, request.createdAt) }){ response: ResponseBody<MyPublicCapsulePageResponseDto> -> response.result.toModel() }
    }
}