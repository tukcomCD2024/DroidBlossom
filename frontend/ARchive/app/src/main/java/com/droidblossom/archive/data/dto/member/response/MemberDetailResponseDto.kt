package com.droidblossom.archive.data.dto.member.response

import com.droidblossom.archive.domain.model.member.MemberDetail
import java.io.Serializable

data class MemberDetailResponseDto(
    val nickname : String,
    val profileUrl : String,
    val phone : String
) : Serializable {

    fun toModel() = MemberDetail(
        nickname = this.nickname,
        profileUrl = this.profileUrl,
        phone = this.phone
    )
}
