package com.droidblossom.archive.data.repository

import com.droidblossom.archive.data.dto.ResponseBody
import com.droidblossom.archive.data.dto.capsule_skin.request.CapsuleSkinsPageRequestDto
import com.droidblossom.archive.data.dto.capsule_skin.response.CapsuleSkinsPageResponseDto
import com.droidblossom.archive.data.source.remote.api.CapsuleSkinService
import com.droidblossom.archive.domain.model.capsule_skin.CapsuleSkinsPageResponse
import com.droidblossom.archive.domain.repository.CapsuleSkinRepository
import com.droidblossom.archive.util.RetrofitResult
import com.droidblossom.archive.util.apiHandler
import javax.inject.Inject

class CapsuleSkinRepositoryImpl @Inject constructor(
    private val api : CapsuleSkinService
) : CapsuleSkinRepository{
    
    override suspend fun getCapsuleSkinPage(request: CapsuleSkinsPageRequestDto): RetrofitResult<CapsuleSkinsPageResponse> {
        return apiHandler({ api.getCapsuleSkinsPageApi(request.size, request.createdAt) }) { response: ResponseBody<CapsuleSkinsPageResponseDto> -> response.result.toModel() }
    }

}