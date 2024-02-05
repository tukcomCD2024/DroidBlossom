package com.droidblossom.archive.domain.repository

import com.droidblossom.archive.data.dto.capsule_skin.request.CapsuleSkinsMakeRequestDto
import com.droidblossom.archive.data.dto.capsule_skin.request.CapsuleSkinsPageRequestDto
import com.droidblossom.archive.data.dto.capsule_skin.request.CapsuleSkinsSearchPageRequestDto
import com.droidblossom.archive.domain.model.capsule_skin.CapsuleSkinsPageResponse
import com.droidblossom.archive.domain.model.capsule_skin.CapsuleSkinsSearchPageResponse
import com.droidblossom.archive.domain.model.common.CapsuleSkinSummary
import com.droidblossom.archive.util.RetrofitResult

interface CapsuleSkinRepository {

    suspend fun getCapsuleSkinPage(request: CapsuleSkinsPageRequestDto) : RetrofitResult<CapsuleSkinsPageResponse>

    suspend fun postCapsuleSkinMake(request: CapsuleSkinsMakeRequestDto) : RetrofitResult<CapsuleSkinSummary>

    suspend fun getCapsuleSkinSearch(request: CapsuleSkinsSearchPageRequestDto) : RetrofitResult<CapsuleSkinsSearchPageResponse>
}