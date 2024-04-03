package com.droidblossom.archive.data.dto.friend.response

import com.droidblossom.archive.domain.model.friend.FriendsSearchResponse
import java.io.Serializable

data class FriendsSearchResponseDto(
    val id: Long,
    val profileUrl: String,
    val nickname: String,
    val isFriend: Boolean
) : Serializable {

    fun toModel() = FriendsSearchResponse(
        id = this.id,
        profileUrl = this.profileUrl,
        nickname = this.nickname,
        isFriend = this.isFriend,
        isChecked = false
    )
}