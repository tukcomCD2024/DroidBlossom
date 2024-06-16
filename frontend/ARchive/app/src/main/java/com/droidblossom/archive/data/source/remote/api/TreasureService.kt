package com.droidblossom.archive.data.source.remote.api

import com.droidblossom.archive.data.dto.ResponseBody
import com.droidblossom.archive.data.dto.treasure.response.TreasureCapsuleOpenDto
import com.droidblossom.archive.data.dto.treasure.response.TreasureCapsuleSummaryDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TreasureService {

    @POST("treasure-capsules/{capsule_id}/open")
    suspend fun postTreasureCapsuleOpenApi(
        @Path("capsule_id") capsuleId: Long
    ): Response<ResponseBody<TreasureCapsuleOpenDto>>

    @GET("treasure-capsules/{capsule_id}/summary")
    suspend fun getTreasureCapsuleSummaryApi(
        @Path("capsule_id") capsuleId: Long
    ): Response<ResponseBody<TreasureCapsuleSummaryDto>>
}