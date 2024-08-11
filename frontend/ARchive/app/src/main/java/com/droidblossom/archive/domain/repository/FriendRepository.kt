package com.droidblossom.archive.domain.repository

import com.droidblossom.archive.data.dto.common.PagingRequestDto
import com.droidblossom.archive.data.dto.friend.request.FriendAcceptRequestDto
import com.droidblossom.archive.data.dto.friend.request.FriendReqRequestDto
import com.droidblossom.archive.data.dto.friend.request.FriendsReqRequestDto
import com.droidblossom.archive.data.dto.friend.request.FriendsSearchPhoneRequestDto
import com.droidblossom.archive.data.dto.friend.request.FriendsSearchRequestDto
import com.droidblossom.archive.domain.model.friend.Friend
import com.droidblossom.archive.domain.model.friend.FriendReqStatusResponse
import com.droidblossom.archive.domain.model.friend.FriendsPage
import com.droidblossom.archive.domain.model.friend.FriendsSearchPhoneResponse
import com.droidblossom.archive.domain.model.friend.FriendsSearchResponse
import com.droidblossom.archive.presentation.model.mypage.detail.FriendForGroupInvite
import com.droidblossom.archive.util.RetrofitResult

interface FriendRepository {

    suspend fun postFriendsRequest(request: FriendReqRequestDto) : RetrofitResult<String>
    suspend fun postFriendsListRequest(request: FriendsReqRequestDto) : RetrofitResult<String>
    suspend fun postFriendsAcceptRequest(request: FriendAcceptRequestDto) : RetrofitResult<String>
    suspend fun postFriendsSearch(request: FriendsSearchRequestDto) : RetrofitResult<FriendsSearchResponse>
    suspend fun postFriendsSearchPhone(request : FriendsSearchPhoneRequestDto) : RetrofitResult<FriendsSearchPhoneResponse>
    suspend fun getFriendsPage(request: PagingRequestDto) : RetrofitResult<FriendsPage<Friend>>
    suspend fun getFriendsForAddGroupPage(request: PagingRequestDto): RetrofitResult<FriendsPage<FriendsSearchResponse>>
    suspend fun getFriendsRequestsPage(request: PagingRequestDto) : RetrofitResult<FriendsPage<Friend>>
    suspend fun getFriendsSendRequestsPage(request: PagingRequestDto) : RetrofitResult<FriendsPage<Friend>>
    suspend fun deleteFriend(friendId: Long) : RetrofitResult<String>
    suspend fun deleteFriendSend(friendId: Long): RetrofitResult<String>
    suspend fun deleteFriendDeny(friendId: Long) : RetrofitResult<String>
    suspend fun getFriendsForGroupInvitePage(groupId : Long, pagingRequestDto: PagingRequestDto) : RetrofitResult<FriendsPage<FriendForGroupInvite>>

}