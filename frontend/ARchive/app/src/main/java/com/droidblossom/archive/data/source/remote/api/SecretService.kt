package com.droidblossom.archive.data.source.remote.api

import com.droidblossom.archive.data.dto.ResponseBody
import com.droidblossom.archive.data.dto.secret.request.SecretCapsuleCreateRequestDto
import com.droidblossom.archive.data.dto.secret.request.SecretCapsulePageRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET

interface SecretService {

    @GET("secrete/capsules")
    suspend fun getSecretCapsulePageApi(
        @Body request : SecretCapsulePageRequestDto
    ) : Response<ResponseBody<SecretCapsuleCreateRequestDto>>

}