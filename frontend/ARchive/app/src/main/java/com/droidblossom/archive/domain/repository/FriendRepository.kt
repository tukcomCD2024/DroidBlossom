package com.droidblossom.archive.domain.repository

import com.droidblossom.archive.data.dto.friend.request.FriendAcceptRequestDto
import com.droidblossom.archive.data.dto.friend.request.FriendReqRequestDto
import com.droidblossom.archive.domain.model.friend.FriendReqStatusResponse
import com.droidblossom.archive.util.RetrofitResult

interface FriendRepository {

    suspend fun postFriendsRequest(request: FriendReqRequestDto) : RetrofitResult<FriendReqStatusResponse>

    suspend fun postFriendsAcceptRequest(request: FriendAcceptRequestDto) : RetrofitResult<String>
}