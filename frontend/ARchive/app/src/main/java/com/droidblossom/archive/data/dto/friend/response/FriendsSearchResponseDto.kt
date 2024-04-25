package com.droidblossom.archive.data.dto.friend.response

import com.droidblossom.archive.domain.model.friend.FriendsSearchResponse
import java.io.Serializable

data class FriendsSearchResponseDto(
    val id: Long,
    val profileUrl: String,
    val originName : String?,
    val nickname: String,
    val isFriend: Boolean,
    val isFriendRequest :Boolean,
) : Serializable {

    fun toModel() = FriendsSearchResponse(
        id = this.id,
        profileUrl = this.profileUrl,
        nickname = this.nickname,
        isFriend = this.isFriend,
        isFriendRequest = this.isFriendRequest,
        name = this.originName ?: "",
        isChecked = false
    )
}