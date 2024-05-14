package com.droidblossom.archive.data.repository

import com.droidblossom.archive.data.dto.ResponseBody
import com.droidblossom.archive.data.dto.capsule_skin.request.CapsuleSkinsMakeRequestDto
import com.droidblossom.archive.data.dto.capsule_skin.request.CapsuleSkinsSearchPageRequestDto
import com.droidblossom.archive.data.dto.capsule_skin.response.CapsuleSkinsMakeResponseDto
import com.droidblossom.archive.data.dto.capsule_skin.response.CapsuleSkinsPageResponseDto
import com.droidblossom.archive.data.dto.capsule_skin.response.CapsuleSkinsSearchPageResponseDto
import com.droidblossom.archive.data.dto.common.PagingRequestDto
import com.droidblossom.archive.data.dto.common.toModel
import com.droidblossom.archive.data.source.remote.api.CapsuleSkinService
import com.droidblossom.archive.domain.model.capsule_skin.CapsuleSkinsMakeResponse
import com.droidblossom.archive.domain.model.capsule_skin.CapsuleSkinsPageResponse
import com.droidblossom.archive.domain.model.capsule_skin.CapsuleSkinsSearchPageResponse
import com.droidblossom.archive.domain.repository.CapsuleSkinRepository
import com.droidblossom.archive.util.RetrofitResult
import com.droidblossom.archive.util.apiHandler
import javax.inject.Inject

class CapsuleSkinRepositoryImpl @Inject constructor(
    private val api : CapsuleSkinService
) : CapsuleSkinRepository{
    
    override suspend fun getCapsuleSkinPage(request: PagingRequestDto): RetrofitResult<CapsuleSkinsPageResponse> {
        return apiHandler({ api.getCapsuleSkinsPageApi(request.size, request.createdAt) }) { response: ResponseBody<CapsuleSkinsPageResponseDto> -> response.result.toModel() }
    }

    override suspend fun postCapsuleSkinMake(request: CapsuleSkinsMakeRequestDto): RetrofitResult<CapsuleSkinsMakeResponse> {
        return apiHandler({api.postCapsuleSkinsApi(request) }) { response: ResponseBody<CapsuleSkinsMakeResponseDto> -> response.result.toModel()}
    }

    override suspend fun deleteCapsuleSkin(capsuleSKinId: Long): RetrofitResult<String> {
        return apiHandler({api.deleteCapsuleSkinsApi(capsuleSkinId = capsuleSKinId)}){response: ResponseBody<String> -> response.result.toModel()}
    }

    override suspend fun modifyCapsuleSkin(capsuleSKinId: Long): RetrofitResult<String> {
        return apiHandler({api.patchCapsuleSkinsModifyApi(capsuleSkinId = capsuleSKinId)}){response: ResponseBody<String> -> response.result.toModel()}
    }

    override suspend fun getCapsuleSkinSearch(request: CapsuleSkinsSearchPageRequestDto): RetrofitResult<CapsuleSkinsSearchPageResponse> {
        return apiHandler({ api.getCapsuleSkinSearchApi(request.capsule_skin_name, request.size, request.capsule_skin_id) }) { response: ResponseBody<CapsuleSkinsSearchPageResponseDto> -> response.result.toModel() }
    }

}