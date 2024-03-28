package com.droidblossom.archive.domain.model.friend

import com.droidblossom.archive.data.dto.friend.request.FriendAcceptRequestDto


data class FriendAcceptRequest (
    val friendId : Double
){
    fun toDto() = FriendAcceptRequestDto(
        friendId = this.friendId,
    )
}
