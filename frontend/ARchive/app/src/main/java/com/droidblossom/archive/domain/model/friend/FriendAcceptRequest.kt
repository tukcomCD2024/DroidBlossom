package com.droidblossom.archive.domain.model.friend

import com.droidblossom.archive.data.dto.friend.request.FriendAcceptRequestDto


data class FriendAcceptRequest (
    val friendId : Long
){
    fun toDto() = FriendAcceptRequestDto(
        friendId = this.friendId,
    )
}
