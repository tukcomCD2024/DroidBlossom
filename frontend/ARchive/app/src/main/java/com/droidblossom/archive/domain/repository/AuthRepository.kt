package com.droidblossom.archive.domain.repository

import com.droidblossom.archive.data.dto.auth.request.SignUpRequestDto
import com.droidblossom.archive.domain.model.auth.TemporaryToken
import com.droidblossom.archive.util.RetrofitResult

interface AuthRepository {

    suspend fun authSignUp(request: SignUpRequestDto) : RetrofitResult<TemporaryToken>
}