package com.droidblossom.archive.domain.repository

import com.droidblossom.archive.data.dto.friend.request.FriendReqRequestDto
import com.droidblossom.archive.domain.model.friend.FriendReqStatusResponse
import com.droidblossom.archive.util.RetrofitResult

interface FriendRepository {

    suspend fun postFriendFriendsRequest(request: FriendReqRequestDto) : RetrofitResult<FriendReqStatusResponse>
}