package com.droidblossom.archive.domain.repository

import com.droidblossom.archive.data.dto.capsule_skin.request.CapsuleSkinsMakeRequestDto
import com.droidblossom.archive.data.dto.capsule_skin.request.CapsuleSkinsSearchPageRequestDto
import com.droidblossom.archive.data.dto.common.PagingRequestDto
import com.droidblossom.archive.domain.model.capsule_skin.CapsuleSkinDeleteResultResponse
import com.droidblossom.archive.domain.model.capsule_skin.CapsuleSkinsMakeResponse
import com.droidblossom.archive.domain.model.capsule_skin.CapsuleSkinsPageResponse
import com.droidblossom.archive.domain.model.capsule_skin.CapsuleSkinsSearchPageResponse
import com.droidblossom.archive.util.RetrofitResult

interface CapsuleSkinRepository {

    suspend fun getCapsuleSkinPage(request: PagingRequestDto) : RetrofitResult<CapsuleSkinsPageResponse>

    suspend fun postCapsuleSkinMake(request: CapsuleSkinsMakeRequestDto) : RetrofitResult<CapsuleSkinsMakeResponse>

    suspend fun deleteCapsuleSkin(capsuleSKinId : Long ) : RetrofitResult<CapsuleSkinDeleteResultResponse>

    suspend fun modifyCapsuleSkin(capsuleSKinId : Long ) : RetrofitResult<String>

    suspend fun getCapsuleSkinSearch(request: CapsuleSkinsSearchPageRequestDto) : RetrofitResult<CapsuleSkinsSearchPageResponse>
}