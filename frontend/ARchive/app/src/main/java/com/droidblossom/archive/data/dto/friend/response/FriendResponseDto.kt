package com.droidblossom.archive.data.dto.friend.response

import com.droidblossom.archive.domain.model.friend.Friend

data class FriendResponseDto(
    val createdAt: String,
    val id: Int,
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
}