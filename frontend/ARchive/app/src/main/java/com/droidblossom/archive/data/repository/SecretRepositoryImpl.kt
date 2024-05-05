package com.droidblossom.archive.data.repository

import com.droidblossom.archive.data.dto.ResponseBody
import com.droidblossom.archive.data.dto.common.toModel
import com.droidblossom.archive.data.dto.common.CapsuleCreateRequestDto
import com.droidblossom.archive.data.dto.secret.request.SecretCapsuleModifyRequestDto
import com.droidblossom.archive.data.dto.common.CapsuleDetailResponseDto
import com.droidblossom.archive.data.dto.secret.response.SecretCapsuleModifyResponseDto
import com.droidblossom.archive.data.dto.secret.response.SecretCapsulePageResponseDto
import com.droidblossom.archive.data.dto.common.CapsuleSummaryResponseDto
import com.droidblossom.archive.data.dto.common.PagingRequestDto
import com.droidblossom.archive.data.source.remote.api.SecretService
import com.droidblossom.archive.domain.model.common.CapsuleDetail
import com.droidblossom.archive.domain.model.secret.SecretCapsuleModify
import com.droidblossom.archive.domain.model.secret.CapsulePageList
import com.droidblossom.archive.domain.model.common.CapsuleSummaryResponse
import com.droidblossom.archive.domain.repository.SecretRepository
import com.droidblossom.archive.util.RetrofitResult
import com.droidblossom.archive.util.apiHandler
import javax.inject.Inject

class SecretRepositoryImpl @Inject constructor(
    private val api: SecretService
) : SecretRepository {

    override suspend fun getSecretCapsulePage(request: PagingRequestDto): RetrofitResult<CapsulePageList> {
        return apiHandler({ api.getSecretCapsulePageApi(request.size, request.createdAt) }) { response: ResponseBody<SecretCapsulePageResponseDto> -> response.result.toModel() }
    }

    override suspend fun createSecretCapsule(request: CapsuleCreateRequestDto): RetrofitResult<String> {
        return apiHandler({ api.postSecretCapsuleApi(request) }) { response: ResponseBody<String> -> response.result.toModel() }
    }

    override suspend fun getSecretCapsuleDetail(capsuleId: Long): RetrofitResult<CapsuleDetail> {
        return apiHandler({ api.getSecretCapsuleDetailApi(capsuleId) }) { response: ResponseBody<CapsuleDetailResponseDto> -> response.result.toModel() }
    }

    override suspend fun getSecretCapsuleSummary (capsuleId: Int) : RetrofitResult<CapsuleSummaryResponse> {
        return apiHandler({ api.getSecretCapsuleSummaryApi(capsuleId) }) { response: ResponseBody<CapsuleSummaryResponseDto> -> response.result.toModel()}
    }

    override suspend fun modifySecretCapsule(
        capsuleId: Int,
        request: SecretCapsuleModifyRequestDto
    ): RetrofitResult<SecretCapsuleModify> {
        return apiHandler({ api.modifySecretCapsuleApi(capsuleId , request) }) { response: ResponseBody<SecretCapsuleModifyResponseDto> -> response.result.toModel() }
    }
}