package com.droidblossom.archive.data.source.remote.api

import com.droidblossom.archive.data.dto.ResponseBody
import com.droidblossom.archive.data.dto.capsule_skin.response.CapsuleSkinsPageResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CapsuleSkinService {

    @GET("capsule-skins")
    suspend fun getCapsuleSkins(
        @Query("size") size : Int,
        @Query("createdAt") createdAt: String
    ) : Response<ResponseBody<CapsuleSkinsPageResponseDto>>
}