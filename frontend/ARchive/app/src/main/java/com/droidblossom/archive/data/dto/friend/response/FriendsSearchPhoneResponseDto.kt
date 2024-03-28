package com.droidblossom.archive.data.dto.friend.response

import com.droidblossom.archive.domain.model.friend.FriendsSearchPhoneResponse
import java.io.Serializable

data class FriendsSearchPhoneResponseDto(
    val friends : List<FriendsSearchResponseDto>
) : Serializable {

    fun toModel() = FriendsSearchPhoneResponse(
        friends = this.friends.map { it.toModel() },
    )
}
