package com.droidblossom.archive.data.source.remote.api

import com.droidblossom.archive.data.dto.ResponseBody
import com.droidblossom.archive.data.dto.common.CapsuleCreateRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface GroupCapsuleService {

    @POST("group-capsules/{group_id}")
    suspend fun postGroupCapsuleApi(
        @Path("group_id") groupId : Long,
        @Body request : CapsuleCreateRequestDto
    ) : Response<ResponseBody<String>>
}