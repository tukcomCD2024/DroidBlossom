package com.droidblossom.archive.data.dto.member.response

import com.droidblossom.archive.domain.model.member.MemberDetail
import java.io.Serializable

data class MemberDetailResponseDto(
    val nickname : String,
    val profileUrl : String,
    val tag : String,
    val socialType : String,
    val email: String,
    val phone: String,
    val friendCount: Int,
    val groupCount: Int,
    val tagSearchAvailable : Boolean,
    val phoneSearchAvailable : Boolean,
) : Serializable {

    fun toModel() = MemberDetail(
        nickname = this.nickname,
        profileUrl = this.profileUrl,
        tag = this.tag,
        socialType = this.socialType,
        email = this.email,
        phone = this.phone,
        friendCount = this.friendCount,
        groupCount = this.groupCount,
        tagSearchAvailable = this.tagSearchAvailable,
        phoneSearchAvailable = this.phoneSearchAvailable,
    )
}
