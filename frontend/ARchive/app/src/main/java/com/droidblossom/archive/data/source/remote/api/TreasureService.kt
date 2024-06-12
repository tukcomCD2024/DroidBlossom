package com.droidblossom.archive.data.source.remote.api

import com.droidblossom.archive.data.dto.ResponseBody
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Path

interface TreasureService {

    @POST("treasure-capsules/{capsule_id}/open")
    suspend fun postTreasureCapsuleOpenApi(
        @Path("capsule_id") capsuleId: Long
    ): Response<ResponseBody<String>>
}