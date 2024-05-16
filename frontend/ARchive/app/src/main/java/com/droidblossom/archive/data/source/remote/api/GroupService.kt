package com.droidblossom.archive.data.source.remote.api

import com.droidblossom.archive.data.dto.ResponseBody
import com.droidblossom.archive.data.dto.group.request.CreateGroupRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface GroupService {

    @POST("groups")
    suspend fun postCrateGroupApi(
        @Body request : CreateGroupRequestDto
    ) : Response<ResponseBody<String>>

}