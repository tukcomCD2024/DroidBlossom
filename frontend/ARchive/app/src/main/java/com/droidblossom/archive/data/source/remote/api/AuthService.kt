package com.droidblossom.archive.data.source.remote.api

import com.droidblossom.archive.data.dto.ResponseBody
import com.droidblossom.archive.data.dto.auth.request.SignInRequestDto
import com.droidblossom.archive.data.dto.auth.request.SignUpRequestDto
import com.droidblossom.archive.data.dto.auth.request.TokenReIssueRequestDto
import com.droidblossom.archive.data.dto.auth.request.VerificationMessageSendRequestDto
import com.droidblossom.archive.data.dto.auth.request.VerificationNumberValidRequestDto
import com.droidblossom.archive.data.dto.auth.response.TemporaryTokenResponseDto
import com.droidblossom.archive.data.dto.auth.response.TokenResponseDto
import com.droidblossom.archive.data.dto.auth.response.VerificationMessageResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {

    @POST("auth/verification/send-message")
    suspend fun postValidSendMessageApi(
        @Body request : VerificationMessageSendRequestDto
    ) : Response<ResponseBody<VerificationMessageResponseDto>>

    @POST("auth/verification/valid-message")
    suspend fun postValidMessageApi(
        @Body request : VerificationNumberValidRequestDto
    ) : Response<ResponseBody<TokenResponseDto>>

    @POST("auth/token/re-issue")
    suspend fun postReIssueApi(
        @Body request : TokenReIssueRequestDto
    ) : Response<ResponseBody<TokenResponseDto>>

    @POST("auth/temporary-token/re-issue")
    suspend fun postTemporaryTokenReIssueApi(
        @Body request : SignInRequestDto
    ) : Response<ResponseBody<TemporaryTokenResponseDto>>

    @POST("auth/sign-up")
    suspend fun postSignUpApi(
        @Body request: SignUpRequestDto
    ) : Response<ResponseBody<TemporaryTokenResponseDto>>

    @POST("auth/sign-in")
    suspend fun postSignIpApi(
        @Body request: SignInRequestDto
    ) : Response<ResponseBody<TokenResponseDto>>

}