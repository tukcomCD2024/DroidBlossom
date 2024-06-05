package com.droidblossom.archive.data.dto.group.response

import com.droidblossom.archive.domain.model.group.GroupInvitedUser

data class GroupInvitedUserResponseDto (
    val id: Long,
    val nickname: String,
    val profileUrl: String,
    val sendingInvitesCreatedAt: String,
){
    fun toModel() = GroupInvitedUser(
        id = this.id,
        nickname = this.nickname,
        profileUrl = this.profileUrl,
        sendingInvitesCreatedAt = this.sendingInvitesCreatedAt
    )
}