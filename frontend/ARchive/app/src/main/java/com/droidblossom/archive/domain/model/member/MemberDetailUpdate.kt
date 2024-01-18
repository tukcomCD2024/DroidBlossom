package com.droidblossom.archive.domain.model.member

import com.droidblossom.archive.data.dto.member.request.MemberDetailUpdateRequestDto

data class MemberDetailUpdate (
    val nickname : String,
    val profileImage : String
){
    fun toDto() = MemberDetailUpdateRequestDto(
        nickname = this.nickname,
        profileImage = this.profileImage.ifBlank { "" }
    )
}