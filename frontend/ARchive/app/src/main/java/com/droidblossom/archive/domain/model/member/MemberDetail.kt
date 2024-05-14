package com.droidblossom.archive.domain.model.member

import com.droidblossom.archive.presentation.model.mypage.ProfileData

data class MemberDetail(
    val nickname : String,
    val profileUrl : String,
    val tag : String,
    val friendCount: Int,
    val groupCount: Int
){
    fun toUIModel() = ProfileData(
        profileId = 0,
        nickname = this.nickname,
        profileUrl = this.profileUrl,
        tag = this.tag,
        friendCount = this.friendCount,
        groupCount = this.groupCount
    )
}