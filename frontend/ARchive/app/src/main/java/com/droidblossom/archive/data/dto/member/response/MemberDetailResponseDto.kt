package com.droidblossom.archive.data.dto.member.response

import com.droidblossom.archive.domain.model.member.MemberDetail
import java.io.Serializable

data class MemberDetailResponseDto(
    val nickname : String,
    val profileUrl : String,
    val tag : String,
    val friendCount: Int,
    val groupCount: Int
) : Serializable {

    fun toModel() = MemberDetail(
        nickname = this.nickname,
        profileUrl = this.profileUrl,
        tag = this.tag,
        friendCount = this.friendCount,
        groupCount = this.groupCount,
    )
}
