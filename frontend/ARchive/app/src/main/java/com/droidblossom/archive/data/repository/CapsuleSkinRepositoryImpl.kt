package com.droidblossom.archive.data.repository

import com.droidblossom.archive.data.dto.ResponseBody
import com.droidblossom.archive.data.dto.capsule_skin.request.CapsuleSkinsMakeRequestDto
import com.droidblossom.archive.data.dto.capsule_skin.request.CapsuleSkinsPageRequestDto
import com.droidblossom.archive.data.dto.capsule_skin.response.CapsuleSkinsPageResponseDto
import com.droidblossom.archive.data.dto.common.CapsuleSkinSummaryResponseDto
import com.droidblossom.archive.data.source.remote.api.CapsuleSkinService
import com.droidblossom.archive.domain.model.capsule_skin.CapsuleSkinsPageResponse
import com.droidblossom.archive.domain.model.common.CapsuleSkinSummary
import com.droidblossom.archive.domain.repository.CapsuleSkinRepository
import com.droidblossom.archive.util.RetrofitResult
import com.droidblossom.archive.util.apiHandler
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import javax.inject.Inject

class CapsuleSkinRepositoryImpl @Inject constructor(
    private val api : CapsuleSkinService
) : CapsuleSkinRepository{
    
    override suspend fun getCapsuleSkinPage(request: CapsuleSkinsPageRequestDto): RetrofitResult<CapsuleSkinsPageResponse> {
        return apiHandler({ api.getCapsuleSkinsPageApi(request.size, request.createdAt) }) { response: ResponseBody<CapsuleSkinsPageResponseDto> -> response.result.toModel() }
    }

    override suspend fun postCapsuleSkinMake(request: CapsuleSkinsMakeRequestDto): RetrofitResult<CapsuleSkinSummary> {
        val fileRequestBody = request.skinImage.asRequestBody("image/jpeg".toMediaType())
        val skinImagePart = MultipartBody.Part.createFormData("skinImage", request.skinImage.name, fileRequestBody)
        return apiHandler({api.postCapsuleSkinsApi(request.name, skinImagePart, request.motionName) }) { response: ResponseBody<CapsuleSkinSummaryResponseDto> -> response.result.toModel()}
    }

}