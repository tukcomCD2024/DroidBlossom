package com.droidblossom.archive.data.repository

import com.droidblossom.archive.data.dto.ResponseBody
import com.droidblossom.archive.data.dto.auth.request.SignUpRequestDto
import com.droidblossom.archive.data.dto.auth.response.TemporaryTokenResponseDto
import com.droidblossom.archive.data.source.remote.api.AuthService
import com.droidblossom.archive.domain.model.auth.TemporaryToken
import com.droidblossom.archive.domain.repository.AuthRepository
import com.droidblossom.archive.util.RetrofitResult
import com.droidblossom.archive.util.apiHandler
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthService
) : AuthRepository {

    override suspend fun authSignUp(request: SignUpRequestDto): RetrofitResult<TemporaryToken> {
        return apiHandler({ api.postSignUpApi(request) }) { response: ResponseBody<TemporaryTokenResponseDto> -> response.result.toModel() }
    }
}