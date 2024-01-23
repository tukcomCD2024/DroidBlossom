package com.droidblossom.archive.domain.repository

import com.droidblossom.archive.data.dto.auth.request.SignInRequestDto
import com.droidblossom.archive.data.dto.auth.request.SignUpRequestDto
import com.droidblossom.archive.data.dto.auth.request.TokenReIssueRequestDto
import com.droidblossom.archive.domain.model.auth.TemporaryToken
import com.droidblossom.archive.domain.model.auth.Token
import com.droidblossom.archive.util.RetrofitResult

interface AuthRepository {


    suspend fun reIssue(request : TokenReIssueRequestDto) : RetrofitResult<Token>

    suspend fun authSignIn(request : SignInRequestDto) : RetrofitResult<Token>
    suspend fun authSignUp(request: SignUpRequestDto) : RetrofitResult<TemporaryToken>
}