package com.droidblossom.archive.data.source.remote.api

import com.droidblossom.archive.data.dto.ResponseBody
import com.droidblossom.archive.data.dto.common.CapsuleCreateRequestDto
import com.droidblossom.archive.data.dto.group.response.MyGroupCapsulePageResponseDto
import com.droidblossom.archive.data.dto.group_capsule.GroupCapsuleDetailResponseDto
import com.droidblossom.archive.data.dto.group_capsule.GroupCapsuleOpenStateResponseDto
import com.droidblossom.archive.data.dto.group_capsule.GroupCapsuleSummaryResponseDto
import com.droidblossom.archive.data.dto.group_capsule.GroupMembersCapsuleOpenStatusResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface GroupCapsuleService {

    @POST("group-capsules/{group_id}")
    suspend fun postGroupCapsuleApi(
        @Path("group_id") groupId : Long,
        @Body request : CapsuleCreateRequestDto
    ) : Response<ResponseBody<String>>

    @POST("group-capsules/{capsule_id}/open")
    suspend fun postGroupCapsuleOpenApi(
        @Path("capsule_id") capsuleId: Long
    ): Response<ResponseBody<GroupCapsuleOpenStateResponseDto>>

    @GET("group-capsules/{capsule_id}/summary")
    suspend fun getGroupCapsuleSummaryApi(
        @Path("capsule_id") capsuleId: Long
    ): Response<ResponseBody<GroupCapsuleSummaryResponseDto>>

    @GET("group-capsules/{capsule_id}/detail")
    suspend fun getGroupCapsuleDetailApi(
        @Path("capsule_id") capsuleId: Long
    ): Response<ResponseBody<GroupCapsuleDetailResponseDto>>


    @GET("group-capsules/my")
    suspend fun getMyGroupCapsulesApi(
        @Query("size") size : Int,
        @Query("created_at") createdAt: String
    ): Response<ResponseBody<MyGroupCapsulePageResponseDto>>

    @GET("group-capsules/{capsule_id}/open-status")
    suspend fun getGroupCapsulesMemberOpenStatusApi(
        @Path("capsule_id") capsuleId: Long,
        @Query("group_id") groupId: Long
    ): Response<ResponseBody<GroupMembersCapsuleOpenStatusResponseDto>>

}