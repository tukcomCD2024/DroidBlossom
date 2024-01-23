package com.droidblossom.archive.data.source.remote.api

import com.droidblossom.archive.data.dto.ResponseBody
import com.droidblossom.archive.data.dto.auth.response.HealthResponseDto
import com.droidblossom.archive.data.dto.member.response.MemberDetailResponseDto
import com.droidblossom.archive.domain.model.auth.Health
import retrofit2.Response
import retrofit2.http.GET

interface MemberService {

    @GET("me/")
    suspend fun getMeApi(): Response<ResponseBody<MemberDetailResponseDto>>

    @GET("health")
    suspend fun getTextApi() : Response<ResponseBody<HealthResponseDto>>
}