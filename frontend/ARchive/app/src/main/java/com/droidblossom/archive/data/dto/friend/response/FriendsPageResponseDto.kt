package com.droidblossom.archive.data.dto.friend.response

import com.droidblossom.archive.domain.model.friend.FriendsPage

data class FriendsPageResponseDto(
    val friends: List<FriendResponseDto>,
    val hasNext: Boolean
) {
    fun toModel() = FriendsPage(
        friends = this.friends.map { it.toModel() },
        hasNext = this.hasNext
    )

    fun toModelForAddGroup() = FriendsPage(
        friends = this.friends.map { it.toFriendsSearchModel() },
        hasNext = this.hasNext
    )
}