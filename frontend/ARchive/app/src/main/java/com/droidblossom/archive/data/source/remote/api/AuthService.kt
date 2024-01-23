package com.droidblossom.archive.data.source.remote.api

import com.droidblossom.archive.data.dto.ResponseBody
import com.droidblossom.archive.data.dto.auth.request.SignUpRequestDto
import com.droidblossom.archive.data.dto.auth.response.TemporaryTokenResponseDto
import com.droidblossom.archive.domain.model.auth.TemporaryToken
import com.droidblossom.archive.util.RetrofitResult
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthService {

    @POST("auth/sign-up")
    suspend fun postSignUpApi(
        @Body request: SignUpRequestDto
    ) : Response<ResponseBody<TemporaryTokenResponseDto>>
}