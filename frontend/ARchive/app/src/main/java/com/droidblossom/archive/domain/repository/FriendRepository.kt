package com.droidblossom.archive.domain.repository

import com.droidblossom.archive.data.dto.friend.request.FriendAcceptRequestDto
import com.droidblossom.archive.data.dto.friend.request.FriendReqRequestDto
import com.droidblossom.archive.data.dto.friend.request.FriendsSearchPhoneRequestDto
import com.droidblossom.archive.domain.model.friend.FriendReqStatusResponse
import com.droidblossom.archive.domain.model.friend.FriendsSearchPhoneResponse
import com.droidblossom.archive.domain.model.friend.FriendsSearchResponse
import com.droidblossom.archive.util.RetrofitResult

interface FriendRepository {

    suspend fun postFriendsRequest(request: FriendReqRequestDto) : RetrofitResult<FriendReqStatusResponse>

    suspend fun postFriendsAcceptRequest(request: FriendAcceptRequestDto) : RetrofitResult<String>

    suspend fun postFriendsSearch() : RetrofitResult<FriendsSearchResponse>
    suspend fun postFriendsSearchPhone(request : FriendsSearchPhoneRequestDto) : RetrofitResult<FriendsSearchPhoneResponse>

}