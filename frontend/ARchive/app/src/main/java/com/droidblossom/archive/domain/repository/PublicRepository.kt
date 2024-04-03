package com.droidblossom.archive.domain.repository

import com.droidblossom.archive.data.dto.common.CapsuleCreateRequestDto
import com.droidblossom.archive.domain.model.common.CapsuleSummaryResponse
import com.droidblossom.archive.util.RetrofitResult

interface PublicRepository {

    suspend fun createPublicCapsule (request: CapsuleCreateRequestDto) : RetrofitResult<String>

    suspend fun getPublicCapsuleSummary (capsuleId: Int) : RetrofitResult<CapsuleSummaryResponse>

}