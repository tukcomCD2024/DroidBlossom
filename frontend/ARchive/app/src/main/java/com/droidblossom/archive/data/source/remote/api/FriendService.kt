package com.droidblossom.archive.data.source.remote.api

import com.droidblossom.archive.data.dto.friend.request.FriendReqRequestDto
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response
import com.droidblossom.archive.data.dto.ResponseBody
import com.droidblossom.archive.data.dto.friend.request.FriendsReqRequestDto
import com.droidblossom.archive.data.dto.friend.request.FriendsSearchPhoneRequestDto
import com.droidblossom.archive.data.dto.friend.response.FriendReqStatusResponseDto
import com.droidblossom.archive.data.dto.friend.response.FriendsPageResponseDto
import com.droidblossom.archive.data.dto.friend.response.FriendsSearchPhoneResponseDto
import com.droidblossom.archive.data.dto.friend.response.FriendsSearchResponseDto
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FriendService {

    @POST("friends/{friend_id}/request")
    suspend fun postFriendsRequestApi(
        @Path("friend_id") friendId : Long,
    ) : Response<ResponseBody<String>>

    @POST("friends/{friend_id}/accept-request")
    suspend fun postFriendsAcceptRequestApi(
        @Path("friend_id") friendId : Long,
    ) : Response<ResponseBody<String>>

    @GET("friends/search")
    suspend fun postFriendsSearchApi(
        @Query("friend_tag") friendTag : String
    ) : Response<ResponseBody<FriendsSearchResponseDto>>

    @POST("friends/search/phone")
    suspend fun postFriendsSearchPhoneApi(
        @Body request : FriendsSearchPhoneRequestDto
    ) : Response<ResponseBody<FriendsSearchPhoneResponseDto>>

    @GET("friends")
    suspend fun getFriendsPageApi(
        @Query("size") size : Int,
        @Query("created_at") createdAt : String,
    ) : Response<ResponseBody<FriendsPageResponseDto>>

    @GET("friends/receiving-invites")
    suspend fun getFriendsRequestsPageApi(
        @Query("size") size : Int,
        @Query("created_at") createdAt : String,
    ) : Response<ResponseBody<FriendsPageResponseDto>>

    @GET("friends/sending-invites")
    suspend fun getFriendsSendRequestsPageApi(
        @Query("size") size : Int,
        @Query("created_at") createdAt : String,
    ) : Response<ResponseBody<FriendsPageResponseDto>>

    @POST("friends/requests")
    suspend fun postFriendListRequestsPageApi(
        @Body request : FriendsReqRequestDto
    ) : Response<ResponseBody<String>>

    @DELETE("friends/{friend_id}")
    suspend fun deleteFriendApi(
        @Path("friend_id") friendId : Long,
    ) : Response<ResponseBody<String>>


    @DELETE("friends/{friend_id}/sending-invites")
    suspend fun deleteFriendSendApi(
        @Path("friend_id") friendId : Long,
    ) : Response<ResponseBody<String>>

    @DELETE("friends/{friend_id}/deny-request")
    suspend fun deleteFriendDenyRequestApi(
        @Path("friend_id") friendId : Long,
    ) : Response<ResponseBody<String>>

    @GET("friends/before/group_invite")
    suspend fun getFriendsForGroupInvitePageApi(
        @Query("group_id") groupId : Long,
        @Query("size") size : Int,
        @Query("created_at") createdAt : String
    ) : Response<ResponseBody<FriendsPageResponseDto>>
}