package com.droidblossom.archive.domain.repository

import com.droidblossom.archive.data.dto.secret.request.SecretCapsuleCreateRequestDto
import com.droidblossom.archive.data.dto.secret.request.SecretCapsulePageRequestDto
import com.droidblossom.archive.data.dto.secret.response.SecretCapsuleCreateResponseDto
import com.droidblossom.archive.domain.model.auth.VerificationMessageResult
import com.droidblossom.archive.domain.model.secret.SecretCapsuleDetail
import com.droidblossom.archive.domain.model.secret.SecretCapsulePage
import com.droidblossom.archive.util.RetrofitResult

interface SecretRepository {

    suspend fun getSecretCapsulePage (request: SecretCapsulePageRequestDto) : RetrofitResult<SecretCapsulePage>

    suspend fun createSecretCapsule (request: SecretCapsuleCreateRequestDto) : RetrofitResult<Nothing>

    suspend fun getSecretCapsuleDetail (capsuleId: Int) : RetrofitResult<SecretCapsuleDetail>
}