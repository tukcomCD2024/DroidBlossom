package com.droidblossom.archive.data.source.remote.api

import com.droidblossom.archive.data.dto.ResponseBody
import com.droidblossom.archive.data.dto.group.request.CreateGroupRequestDto
import com.droidblossom.archive.data.dto.group.response.GroupPageResponseDto
import com.droidblossom.archive.data.dto.open.response.PublicCapsuleSliceResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
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

}