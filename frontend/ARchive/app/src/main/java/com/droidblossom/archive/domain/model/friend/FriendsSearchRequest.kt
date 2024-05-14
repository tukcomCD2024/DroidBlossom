package com.droidblossom.archive.domain.model.friend

import com.droidblossom.archive.data.dto.friend.request.FriendsSearchRequestDto

data class FriendsSearchRequest (
    val friendTag : String
){
    fun toDto() = FriendsSearchRequestDto(
        friendTag = this.friendTag,
    )
}