package com.droidblossom.archive.data.dto.group_capsule

import com.droidblossom.archive.domain.model.group_capsule.GroupCapsuleMember

data class GroupCapsuleMemberDto(
    val id: Long,
    val nickname : String,
    val profileUrl : String,
    val isGroupOwner: Boolean,
    val isOpened : Boolean,
){
    fun toModel() = GroupCapsuleMember(
        memberId = this.id,
        nickname = this.nickname,
        profileUrl = this.profileUrl,
        isGroupOwner = this.isGroupOwner,
        isOpened = this.isOpened
    )
}
