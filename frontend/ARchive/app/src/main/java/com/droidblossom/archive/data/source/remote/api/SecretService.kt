package com.droidblossom.archive.data.source.remote.api

import com.droidblossom.archive.data.dto.ResponseBody
import com.droidblossom.archive.data.dto.secret.request.SecretCapsuleCreateRequestDto
import com.droidblossom.archive.data.dto.secret.request.SecretCapsulePageRequestDto
import com.droidblossom.archive.data.dto.secret.response.SecretCapsuleCreateResponseDto
import com.droidblossom.archive.data.dto.secret.response.SecretCapsulePageResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SecretService {

    @GET("secrete/capsules")
    suspend fun getSecretCapsulePageApi(
        @Body request : SecretCapsulePageRequestDto
    ) : Response<ResponseBody<SecretCapsulePageResponseDto>>

    @POST("secrete/capsules")
    suspend fun postSecretCapsuleApi(
        @Body request : SecretCapsuleCreateRequestDto
    ) : Response<ResponseBody<SecretCapsuleCreateResponseDto>>

}