package com.droidblossom.archive.domain.model.friend

import com.droidblossom.archive.data.dto.friend.request.FriendsSearchPhoneRequestDto
import com.droidblossom.archive.data.dto.friend.request.PhoneBooks

data class FriendsSearchPhoneRequest (
    val phoneBooks : List<PhoneBooks>
){
    fun toDto() = FriendsSearchPhoneRequestDto(
        phoneBooks = this.phoneBooks,
    )
}
