package com.droidblossom.archive.data.dto.group.response

import com.droidblossom.archive.domain.model.group.GroupInviteSummary
import com.droidblossom.archive.domain.model.group.GroupPage

data class GroupInviteResponseDto(
    val groupReceivingInviteTime: String,
    val description: String,
    val groupId: Long,
    val groupName: String,
    val groupOwnerName: String,
    val groupProfileUrl: String
) {
    fun toModel() = GroupInviteSummary(
        createdAt = this.groupReceivingInviteTime,
        description = this.description,
        groupId = this.groupId,
        groupName = this.groupName,
        groupOwnerName = this.groupOwnerName,
        groupProfileUrl = this.groupProfileUrl,
    )
}