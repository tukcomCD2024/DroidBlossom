package com.droidblossom.archive.data.dto.group_capsule

import com.droidblossom.archive.domain.model.group_capsule.GroupCapsuleMember

data class GroupCapsuleMemberDto(
    val nickname : String,
    val profileUrl : String,
    val isOpened : Boolean,
){
    fun toModel() = GroupCapsuleMember(
        nickname = this.nickname,
        profileUrl = this.profileUrl,
        isOpened = this.isOpened
    )
}
