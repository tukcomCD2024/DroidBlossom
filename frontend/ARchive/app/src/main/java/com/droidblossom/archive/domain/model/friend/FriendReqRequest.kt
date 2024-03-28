package com.droidblossom.archive.domain.model.friend

import com.droidblossom.archive.data.dto.friend.request.FriendReqRequestDto

data class FriendReqRequest(
    val friendId : Double
){
    fun toDto() = FriendReqRequestDto(
        friendId = this.friendId,
    )
}
