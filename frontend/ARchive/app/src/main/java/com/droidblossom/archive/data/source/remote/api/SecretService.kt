package com.droidblossom.archive.data.source.remote.api

import com.droidblossom.archive.data.dto.ResponseBody
import com.droidblossom.archive.data.dto.secret.request.SecretCapsuleCreateRequestDto
import com.droidblossom.archive.data.dto.secret.request.SecretCapsuleModifyRequestDto
import com.droidblossom.archive.data.dto.secret.request.SecretCapsulePageRequestDto
import com.droidblossom.archive.data.dto.secret.response.SecretCapsuleCreateResponseDto
import com.droidblossom.archive.data.dto.secret.response.SecretCapsuleDetailResponseDto
import com.droidblossom.archive.data.dto.secret.response.SecretCapsulePageResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SecretService {

    @GET("secrete/capsules")
    suspend fun getSecretCapsulePageApi(
        @Query("size") size : Int,
        @Query("capsule_id") capsuleId: Int
    ) : Response<ResponseBody<SecretCapsulePageResponseDto>>

    @POST("secrete/capsules")
    suspend fun postSecretCapsuleApi(
        @Body request : SecretCapsuleCreateRequestDto
    ) : Response<ResponseBody<SecretCapsuleCreateResponseDto>>

    @GET("secrete/capsules/{capsule_id}")
    suspend fun getSecretCapsuleDetailApi(
        @Path("capsule_id") capsuleId : Int,
    ) : Response<ResponseBody<SecretCapsuleDetailResponseDto>>

    //미정 (1/27 기준)
    @PATCH("secrete/capsules/{capsule_id}")
    suspend fun modifySecretCapsuleDetailApi(
        @Path("capsule_id") capsuleId : Int,
        @Body request : SecretCapsuleModifyRequestDto
    ) : Response<ResponseBody<SecretCapsuleDetailResponseDto>>

}