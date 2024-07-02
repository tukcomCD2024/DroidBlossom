package com.droidblossom.archive.domain.repository

import com.droidblossom.archive.data.dto.auth.request.SignInRequestDto
import com.droidblossom.archive.data.dto.auth.request.SignUpRequestDto
import com.droidblossom.archive.data.dto.auth.request.TokenReIssueRequestDto
import com.droidblossom.archive.data.dto.auth.request.VerificationMessageSendRequestDto
import com.droidblossom.archive.data.dto.auth.request.VerificationNumberValidRequestDto
import com.droidblossom.archive.domain.model.auth.TemporaryToken
import com.droidblossom.archive.domain.model.auth.Token
import com.droidblossom.archive.domain.model.auth.VerificationMessageResult
import com.droidblossom.archive.util.RetrofitResult

interface AuthRepository {

    suspend fun authValidMessageSend(request : VerificationMessageSendRequestDto) : RetrofitResult<VerificationMessageResult>
    suspend fun authValidMessage(request : VerificationNumberValidRequestDto) : RetrofitResult<Token>
    suspend fun authReIssue(request : TokenReIssueRequestDto) : RetrofitResult<Token>

    suspend fun authTemporaryTokenReIssue(request : SignInRequestDto) : RetrofitResult<TemporaryToken>

    suspend fun authSignIn(request : SignInRequestDto) : RetrofitResult<Token>
    suspend fun authSignUp(request: SignUpRequestDto) : RetrofitResult<TemporaryToken>

    suspend fun authSignOut(): RetrofitResult<String>
}