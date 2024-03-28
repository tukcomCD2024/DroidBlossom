package com.droidblossom.archive.data.source.remote.api

import com.droidblossom.archive.data.dto.friend.request.FriendReqRequestDto
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response
import com.droidblossom.archive.data.dto.ResponseBody
import com.droidblossom.archive.data.dto.friend.response.FriendReqStatusResponseDto
import retrofit2.http.Query

interface FriendService {

    @POST("friends/{friend_id/request}")
    suspend fun postFriendsRequestApi(
        @Query("friend_id") friendId : Double,
    ) : Response<ResponseBody<FriendReqStatusResponseDto>>

    @POST("friends/{friend_id/accept-request}")
    suspend fun postFriendsAcceptRequestApi(
        @Query("friend_id") friendId : Double,
    ) : Response<ResponseBody<String>>
}