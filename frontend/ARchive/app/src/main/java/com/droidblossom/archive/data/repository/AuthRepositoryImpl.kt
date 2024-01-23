package com.droidblossom.archive.data.repository

import com.droidblossom.archive.data.dto.ResponseBody
import com.droidblossom.archive.data.dto.auth.request.SignInRequestDto
import com.droidblossom.archive.data.dto.auth.request.SignUpRequestDto
import com.droidblossom.archive.data.dto.auth.request.TokenReIssueRequestDto
import com.droidblossom.archive.data.dto.auth.request.VerificationMessageSendRequestDto
import com.droidblossom.archive.data.dto.auth.request.VerificationNumberValidRequestDto
import com.droidblossom.archive.data.dto.auth.response.TemporaryTokenResponseDto
import com.droidblossom.archive.data.dto.auth.response.TokenResponseDto
import com.droidblossom.archive.data.dto.auth.response.VerificationMessageResponseDto
import com.droidblossom.archive.data.source.remote.api.AuthService
import com.droidblossom.archive.domain.model.auth.TemporaryToken
import com.droidblossom.archive.domain.model.auth.Token
import com.droidblossom.archive.domain.model.auth.VerificationMessageResult
import com.droidblossom.archive.domain.repository.AuthRepository
import com.droidblossom.archive.util.RetrofitResult
import com.droidblossom.archive.util.apiHandler
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthService
) : AuthRepository {
    override suspend fun validMessageSend(request: VerificationMessageSendRequestDto): RetrofitResult<VerificationMessageResult>{
        return apiHandler({ api.postValidSendMessageApi(request) }) { response : ResponseBody<VerificationMessageResponseDto> -> response.result.toModel()}

    }

    override suspend fun validMessage(request: VerificationNumberValidRequestDto): RetrofitResult<Token> {
        return apiHandler({ api.postValidMessageApi(request) }) { response : ResponseBody<TokenResponseDto> -> response.result.toModel()}
    }

    override suspend fun reIssue(request: TokenReIssueRequestDto): RetrofitResult<Token> {
        return apiHandler({ api.postReIssueApi(request) }) { response : ResponseBody<TokenResponseDto> -> response.result.toModel()}
    }


    override suspend fun authSignIn(request: SignInRequestDto) : RetrofitResult<Token> {
        return apiHandler({ api.postSignIpApi(request) }) { response : ResponseBody<TokenResponseDto> -> response.result.toModel()}
    }
    override suspend fun authSignUp(request: SignUpRequestDto): RetrofitResult<TemporaryToken> {
        return apiHandler({ api.postSignUpApi(request) }) { response: ResponseBody<TemporaryTokenResponseDto> -> response.result.toModel() }
    }
}