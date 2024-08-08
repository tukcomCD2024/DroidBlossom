package com.droidblossom.archive.data.source.remote.api

import com.droidblossom.archive.data.dto.ResponseBody
import com.droidblossom.archive.data.dto.auth.response.HealthResponseDto
import com.droidblossom.archive.data.dto.member.request.MemberStatusRequestDto
import com.droidblossom.archive.data.dto.member.response.MemberStatusResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UnAuthenticatedService {

    @GET("health")
    suspend fun getServerCheckApi(): Response<ResponseBody<HealthResponseDto>>

    @POST("me/status")
    suspend fun postMeStatusApi(
        @Body checkStatusRequestDto: MemberStatusRequestDto
    ): Response<ResponseBody<MemberStatusResponseDto>>
}