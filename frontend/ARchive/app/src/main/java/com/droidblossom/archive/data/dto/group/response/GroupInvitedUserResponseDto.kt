package com.droidblossom.archive.data.dto.group.response

import com.droidblossom.archive.domain.model.group.GroupInvitedUser

data class GroupInvitedUserResponseDto (
    val groupInviteId: Long,
    val memberId: Long,
    val nickname: String,
    val profileUrl: String,
    val sendingInvitesCreatedAt: String,
){
    fun toModel() = GroupInvitedUser(
        groupInviteId = this.groupInviteId,
        memberId = this.memberId,
        nickname = this.nickname,
        profileUrl = this.profileUrl,
        sendingInvitesCreatedAt = this.sendingInvitesCreatedAt
    )
}