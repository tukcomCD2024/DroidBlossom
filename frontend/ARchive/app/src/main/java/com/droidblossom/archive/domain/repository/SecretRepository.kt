package com.droidblossom.archive.domain.repository

import com.droidblossom.archive.data.dto.common.CapsuleCreateRequestDto
import com.droidblossom.archive.data.dto.common.PagingRequestDto
import com.droidblossom.archive.data.dto.secret.request.SecretCapsuleModifyRequestDto
import com.droidblossom.archive.domain.model.common.CapsuleDetail
import com.droidblossom.archive.domain.model.secret.SecretCapsuleModify
import com.droidblossom.archive.domain.model.secret.CapsulePageList
import com.droidblossom.archive.domain.model.common.CapsuleSummaryResponse
import com.droidblossom.archive.util.RetrofitResult

interface SecretRepository {

    suspend fun getSecretCapsulePage (request: PagingRequestDto) : RetrofitResult<CapsulePageList>

    suspend fun createSecretCapsule (request: CapsuleCreateRequestDto) : RetrofitResult<String>

    suspend fun getSecretCapsuleDetail (capsuleId: Long) : RetrofitResult<CapsuleDetail>

    suspend fun getSecretCapsuleSummary (capsuleId: Long) : RetrofitResult<CapsuleSummaryResponse>
    suspend fun modifySecretCapsule (capsuleId: Long, request: SecretCapsuleModifyRequestDto) : RetrofitResult<SecretCapsuleModify>
}