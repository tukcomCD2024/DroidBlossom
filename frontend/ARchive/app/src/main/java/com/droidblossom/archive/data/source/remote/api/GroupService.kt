package com.droidblossom.archive.data.source.remote.api

import com.droidblossom.archive.data.dto.ResponseBody
import com.droidblossom.archive.data.dto.group.request.CreateGroupRequestDto
import com.droidblossom.archive.data.dto.group.response.GroupDetailResponseDto
import com.droidblossom.archive.data.dto.group.response.GroupPageResponseDto
import com.droidblossom.archive.data.dto.open.response.PublicCapsuleSliceResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface GroupService {

    @POST("groups")
    suspend fun postCrateGroupApi(
        @Body request : CreateGroupRequestDto
    ) : Response<ResponseBody<String>>

    @GET("groups")
    suspend fun getGroupsPageApi(
        @Query("size") size : Int,
        @Query("created_at") createdAt: String
    ) : Response<ResponseBody<GroupPageResponseDto>>

    @POST("groups/{group_id}/member/{target_id}/accept")
    suspend fun postAcceptGroupInviteApi(
        @Path("group_id") groupId : Long,
        @Path("target_id") targetId : Long,
    ) : Response<ResponseBody<String>>

    @DELETE("groups/{group_id}/member/{target_id}/reject")
    suspend fun deleteRejectGroupInviteApi(
        @Path("group_id") groupId : Long,
        @Path("target_id") targetId : Long,
    ) : Response<ResponseBody<String>>

    @GET("groups/{group_id}")
    suspend fun getGroupsDetailApi(
        @Path("group_id") groupId : Long,
    ) : Response<ResponseBody<GroupDetailResponseDto>>

}