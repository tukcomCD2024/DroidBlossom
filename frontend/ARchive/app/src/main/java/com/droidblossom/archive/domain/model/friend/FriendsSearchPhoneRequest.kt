package com.droidblossom.archive.domain.model.friend

import com.droidblossom.archive.data.dto.friend.request.FriendsSearchPhoneRequestDto

data class FriendsSearchPhoneRequest (
    val phones : List<String>
){
    fun toDto() = FriendsSearchPhoneRequestDto(
        phones = this.phones,
    )
}
