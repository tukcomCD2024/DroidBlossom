package com.droidblossom.archive.data.dto.friend.response

import com.droidblossom.archive.domain.model.friend.FriendsSearchResponse
import java.io.Serializable

data class FriendsSearchResponseDto(
    val id: Long,
    val profileUrl: String,
    val originName : String?,
    val tag: String?,
    val nickname: String,
    val isFriend: Boolean,
    val isFriendInviteToFriend : Boolean,
    val isFriendInviteToMe : Boolean,
    val isFriendRequest :Boolean,
) : Serializable {

    fun toModel() = FriendsSearchResponse(
        createdAt = "",
        id = this.id,
        profileUrl = this.profileUrl,
        nickname = this.nickname,
        isFriend = this.isFriend,
        isFriendInviteToFriend = this.isFriendInviteToFriend,
        isFriendInviteToMe = this.isFriendInviteToMe,
        name = this.originName ?: "",
        tag = this.tag ?: "",
        isChecked = false
    )
}