package com.droidblossom.archive.data.dto.group_capsule

import com.droidblossom.archive.domain.model.group_capsule.GroupCapsuleMember

data class GroupCapsuleMemberDto(
    val memberId: Long,
    val nickname : String,
    val profileUrl : String,
    val isOpened : Boolean,
){
    fun toModel() = GroupCapsuleMember(
        memberId = this.memberId,
        nickname = this.nickname,
        profileUrl = this.profileUrl,
        isOpened = this.isOpened
    )
}
