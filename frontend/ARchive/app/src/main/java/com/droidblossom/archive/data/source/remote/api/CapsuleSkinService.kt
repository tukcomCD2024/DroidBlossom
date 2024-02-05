package com.droidblossom.archive.data.source.remote.api

import com.droidblossom.archive.data.dto.ResponseBody
import com.droidblossom.archive.data.dto.capsule_skin.response.CapsuleSkinsPageResponseDto
import com.droidblossom.archive.data.dto.common.CapsuleSkinSummaryResponseDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface CapsuleSkinService {

    @GET("capsule-skins")
    suspend fun getCapsuleSkinsPageApi(
        @Query("size") size : Int,
        @Query("createdAt") createdAt: String
    ) : Response<ResponseBody<CapsuleSkinsPageResponseDto>>

    @Multipart
    @POST("capsule-skins")
    suspend fun postCapsuleSkinsApi(
        @Part("name") name: String,
        @Part skinImage: MultipartBody.Part,
        @Part("motionName") motionName: String
    ): Response<ResponseBody<CapsuleSkinSummaryResponseDto>>
}