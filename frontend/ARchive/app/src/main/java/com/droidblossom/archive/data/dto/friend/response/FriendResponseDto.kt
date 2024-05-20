package com.droidblossom.archive.data.dto.friend.response

import com.droidblossom.archive.domain.model.friend.Friend
import com.droidblossom.archive.domain.model.friend.FriendsSearchResponse

data class FriendResponseDto(
    val createdAt: String,
    val id: Long,
    val nickname: String,
    val profileUrl: String
) {
    fun toModel() = Friend(
        createdAt = this.createdAt,
        id = this.id,
        nickname = this.nickname,
        profileUrl = this.profileUrl,
        isOpenDelete = false
    )

    fun toFriendsSearchModel() = FriendsSearchResponse(
        createdAt = "",
        id = this.id,
        profileUrl = this.profileUrl,
        nickname = this.nickname,
        isFriend = false,
        isFriendInviteToFriend = false,
        isFriendInviteToMe = false,
        name = "",
        isChecked = false
    )
}