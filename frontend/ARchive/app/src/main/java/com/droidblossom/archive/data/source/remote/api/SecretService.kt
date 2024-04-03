package com.droidblossom.archive.data.source.remote.api

import com.droidblossom.archive.data.dto.ResponseBody
import com.droidblossom.archive.data.dto.common.CapsuleCreateRequestDto
import com.droidblossom.archive.data.dto.secret.request.SecretCapsuleModifyRequestDto
import com.droidblossom.archive.data.dto.secret.response.SecretCapsuleDetailResponseDto
import com.droidblossom.archive.data.dto.secret.response.SecretCapsuleModifyResponseDto
import com.droidblossom.archive.data.dto.secret.response.SecretCapsulePageResponseDto
import com.droidblossom.archive.data.dto.common.CapsuleSummaryResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SecretService {

    @GET("secret/capsules")
    suspend fun getSecretCapsulePageApi(
        @Query("size") size : Int,
        @Query("createdAt") createdAt: String
    ) : Response<ResponseBody<SecretCapsulePageResponseDto>>

    @POST("capsules/secret")
    suspend fun postSecretCapsuleApi(
        @Body request : CapsuleCreateRequestDto
    ) : Response<ResponseBody<String>>

    @GET("secret/capsules/{capsule_id}/detail")
    suspend fun getSecretCapsuleDetailApi(
        @Path("capsule_id") capsuleId : Long,
    ) : Response<ResponseBody<SecretCapsuleDetailResponseDto>>

    @GET("secret/capsules/{capsule_id}/summary")
    suspend fun getSecretCapsuleSummaryApi(
        @Path("capsule_id") capsuleId : Int,
    ) : Response<ResponseBody<CapsuleSummaryResponseDto>>

    //미정 (1/27 기준)
    @PATCH("secret/capsules/{capsule_id}")
    suspend fun modifySecretCapsuleApi(
        @Path("capsule_id") capsuleId : Int,
        @Body request : SecretCapsuleModifyRequestDto
    ) : Response<ResponseBody<SecretCapsuleModifyResponseDto>>

}