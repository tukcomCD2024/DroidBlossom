package com.droidblossom.archive.domain.repository

import com.droidblossom.archive.data.dto.friend.request.FriendAcceptRequestDto
import com.droidblossom.archive.data.dto.friend.request.FriendReqRequestDto
import com.droidblossom.archive.data.dto.friend.request.FriendsSearchPhoneRequestDto
import com.droidblossom.archive.data.dto.friend.request.FriendsSearchRequestDto
import com.droidblossom.archive.domain.model.friend.FriendReqStatusResponse
import com.droidblossom.archive.domain.model.friend.FriendsPage
import com.droidblossom.archive.domain.model.friend.FriendsSearchPhoneResponse
import com.droidblossom.archive.domain.model.friend.FriendsSearchResponse
import com.droidblossom.archive.util.RetrofitResult

interface FriendRepository {

    suspend fun postFriendsRequest(request: FriendReqRequestDto) : RetrofitResult<FriendReqStatusResponse>
    suspend fun postFriendsAcceptRequest(request: FriendAcceptRequestDto) : RetrofitResult<String>
    suspend fun postFriendsSearch(request: FriendsSearchRequestDto) : RetrofitResult<FriendsSearchResponse>
    suspend fun postFriendsSearchPhone(request : FriendsSearchPhoneRequestDto) : RetrofitResult<FriendsSearchPhoneResponse>
    suspend fun getFriendsPage(size: Int ,createdAt : String) : RetrofitResult<FriendsPage>
    suspend fun getFriendsRequestsPage(size: Int, createdAt: String) : RetrofitResult<FriendsPage>
    suspend fun deleteFriend(friendId: Long) : RetrofitResult<String>
    suspend fun deleteFriendDeny(friendId: Long) : RetrofitResult<String>
}