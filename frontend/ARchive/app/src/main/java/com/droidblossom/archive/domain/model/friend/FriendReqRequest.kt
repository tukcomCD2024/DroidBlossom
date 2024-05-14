package com.droidblossom.archive.domain.model.friend

import com.droidblossom.archive.data.dto.friend.request.FriendReqRequestDto

data class FriendReqRequest(
    val friendId : Long
){
    fun toDto() = FriendReqRequestDto(
        friendId = this.friendId,
    )
}
