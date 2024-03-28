package com.droidblossom.archive.data.repository

import com.droidblossom.archive.data.dto.ResponseBody
import com.droidblossom.archive.data.dto.capsule_skin.response.CapsuleSkinsPageResponseDto
import com.droidblossom.archive.data.dto.common.toModel
import com.droidblossom.archive.data.dto.friend.request.FriendAcceptRequestDto
import com.droidblossom.archive.data.dto.friend.request.FriendReqRequestDto
import com.droidblossom.archive.data.dto.friend.response.FriendReqStatusResponseDto
import com.droidblossom.archive.data.source.remote.api.FriendService
import com.droidblossom.archive.domain.model.friend.FriendReqStatusResponse
import com.droidblossom.archive.domain.repository.FriendRepository
import com.droidblossom.archive.util.RetrofitResult
import com.droidblossom.archive.util.apiHandler
import javax.inject.Inject

class FriendRepositoryImpl @Inject constructor(
    private val api : FriendService
) : FriendRepository {
    override suspend fun postFriendsRequest(request: FriendReqRequestDto): RetrofitResult<FriendReqStatusResponse> {
        return apiHandler({ api.postFriendsRequestApi(request.friendId) }) { response: ResponseBody<FriendReqStatusResponseDto> -> response.result.toModel() }
    }

    override suspend fun postFriendsAcceptRequest(request: FriendAcceptRequestDto): RetrofitResult<String> {
        return apiHandler({ api.postFriendsAcceptRequestApi(request.friendId) }) {response: ResponseBody<String> -> response.result.toModel()}
    }
}