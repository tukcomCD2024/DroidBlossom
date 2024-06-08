package com.droidblossom.archive.data.dto.group.response

import com.droidblossom.archive.domain.model.group.GroupInvitedUsersPage

data class GroupInvitedUsersPageResponseDto (
    val groupSendingInviteMembers: List<GroupInvitedUserResponseDto>,
    val hasNext: Boolean
){
    fun toModel() = GroupInvitedUsersPage(
        groupSendingInviteMembers = this.groupSendingInviteMembers.map { it.toModel() },
        hasNext = this.hasNext
    )
}