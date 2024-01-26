package com.droidblossom.archive.data.repository

import com.droidblossom.archive.data.dto.ResponseBody
import com.droidblossom.archive.data.dto.secret.request.SecretCapsulePageRequestDto
import com.droidblossom.archive.data.dto.secret.response.SecretCapsulePageResponseDto
import com.droidblossom.archive.data.source.remote.api.SecretService
import com.droidblossom.archive.domain.model.secret.SecretCapsulePage
import com.droidblossom.archive.domain.repository.SecretRepository
import com.droidblossom.archive.util.RetrofitResult
import com.droidblossom.archive.util.apiHandler
import javax.inject.Inject

class SecretRepositoryImpl @Inject constructor(
    private val api: SecretService
) : SecretRepository {

    override suspend fun getSecretCapsulePage(request: SecretCapsulePageRequestDto): RetrofitResult<SecretCapsulePage> {
        return apiHandler({ api.getSecretCapsulePageApi(request) }) { response: ResponseBody<SecretCapsulePageResponseDto> -> response.result.toModel() }
    }
}