package com.droidblossom.archive.data.source.remote.api

import com.droidblossom.archive.data.dto.friend.request.FriendReqRequestDto
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response
import com.droidblossom.archive.data.dto.ResponseBody
import com.droidblossom.archive.data.dto.friend.request.FriendsSearchPhoneRequestDto
import com.droidblossom.archive.data.dto.friend.response.FriendReqStatusResponseDto
import com.droidblossom.archive.data.dto.friend.response.FriendsPageResponseDto
import com.droidblossom.archive.data.dto.friend.response.FriendsSearchPhoneResponseDto
import com.droidblossom.archive.data.dto.friend.response.FriendsSearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FriendService {

    @POST("friends/{friend_id/request}")
    suspend fun postFriendsRequestApi(
        @Path("friend_id") friendId : Double,
    ) : Response<ResponseBody<FriendReqStatusResponseDto>>

    @POST("friends/{friend_id/accept-request}")
    suspend fun postFriendsAcceptRequestApi(
        @Path("friend_id") friendId : Double,
    ) : Response<ResponseBody<String>>

    @POST("friends/search")
    suspend fun postFriendsSearchApi(
        @Query("friend-tag") friendTag : String
    ) : Response<ResponseBody<FriendsSearchResponseDto>>

    @POST("friends/search/phone")
    suspend fun postFriendsSearchPhoneApi(
        @Body request : FriendsSearchPhoneRequestDto
    ) : Response<ResponseBody<FriendsSearchPhoneResponseDto>>

    @GET("friends")
    suspend fun getFriends(
        @Query("size") size : Int,
        @Query("createdAt") createdAt : String,
    ) : Response<ResponseBody<FriendsPageResponseDto>>
}