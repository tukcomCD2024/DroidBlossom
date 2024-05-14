package com.droidblossom.archive.domain.repository

import com.droidblossom.archive.data.dto.common.CapsuleCreateRequestDto
import com.droidblossom.archive.data.dto.common.PagingRequestDto
import com.droidblossom.archive.domain.model.common.CapsuleDetail
import com.droidblossom.archive.domain.model.common.CapsuleSummaryResponse
import com.droidblossom.archive.domain.model.open.PublicCapsuleSliceResponse
import com.droidblossom.archive.domain.model.secret.CapsulePageList
import com.droidblossom.archive.util.RetrofitResult

interface PublicRepository {

    suspend fun createPublicCapsule (request: CapsuleCreateRequestDto) : RetrofitResult<String>

    suspend fun getPublicCapsuleSummary (capsuleId: Long) : RetrofitResult<CapsuleSummaryResponse>

    suspend fun getPublicCapsuleDetail (capsuleId: Long) : RetrofitResult<CapsuleDetail>

    suspend fun getPublicCapsulesPage(request: PagingRequestDto) : RetrofitResult<PublicCapsuleSliceResponse>

    suspend fun getMyPublicCapsulesPage(request: PagingRequestDto) : RetrofitResult<CapsulePageList>
}