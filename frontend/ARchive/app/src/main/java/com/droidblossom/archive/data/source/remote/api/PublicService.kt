package com.droidblossom.archive.data.source.remote.api

import com.droidblossom.archive.data.dto.ResponseBody
import com.droidblossom.archive.data.dto.common.CapsuleCreateRequestDto
import com.droidblossom.archive.data.dto.common.CapsuleDetailResponseDto
import com.droidblossom.archive.data.dto.common.CapsuleSummaryResponseDto
import com.droidblossom.archive.data.dto.open.response.MyPublicCapsulePageResponseDto
import com.droidblossom.archive.data.dto.open.response.PublicCapsuleSliceResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PublicService {

    @POST("capsules/public")
    suspend fun postPublicCapsuleApi(
        @Body request : CapsuleCreateRequestDto
    ) : Response<ResponseBody<String>>

    @GET("public/capsules/{capsule_id}/summary")
    suspend fun getPublicCapsuleSummaryApi(
        @Path("capsule_id") capsuleId : Int,
    ) : Response<ResponseBody<CapsuleSummaryResponseDto>>

    @GET("public/capsules/{capsule_id}/detail")
    suspend fun getPublicCapsuleDetailApi(
        @Path("capsule_id") capsuleId : Long,
    ) : Response<ResponseBody<CapsuleDetailResponseDto>>

    @GET("public/capsules")
    suspend fun getPublicCapsulesPageApi(
        @Query("size") size : Int,
        @Query("created_at") createdAt: String
    ) : Response<ResponseBody<PublicCapsuleSliceResponseDto>>

    @GET("public-capsules/my")
    suspend fun getMyPublicCapsulePageApi(
        @Query("size") size : Int,
        @Query("created_at") createdAt: String
    ) : Response<ResponseBody<MyPublicCapsulePageResponseDto>>
}