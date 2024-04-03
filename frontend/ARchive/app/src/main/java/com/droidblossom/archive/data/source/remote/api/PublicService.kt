package com.droidblossom.archive.data.source.remote.api

import com.droidblossom.archive.data.dto.ResponseBody
import com.droidblossom.archive.data.dto.common.CapsuleCreateRequestDto
import com.droidblossom.archive.data.dto.common.CapsuleSummaryResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PublicService {

    @POST("capsules/public")
    suspend fun postPublicCapsuleApi(
        @Body request : CapsuleCreateRequestDto
    ) : Response<ResponseBody<String>>

    @GET("public/capsules/{capsule_id}/summary")
    suspend fun getPublicCapsuleSummaryApi(
        @Path("capsule_id") capsuleId : Int,
    ) : Response<ResponseBody<CapsuleSummaryResponseDto>>


}