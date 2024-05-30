package com.droidblossom.archive.data.dto.group.response

import com.droidblossom.archive.domain.model.group.GroupInviteSummary
import com.droidblossom.archive.domain.model.group.GroupPage

data class GroupInviteResponseDto(
    val createdAt: String,
    val description: String,
    val groupId: Int,
    val groupName: String,
    val groupOwnerName: String,
    val groupProfileUrl: String
) {
    fun toModel() = GroupInviteSummary(
        createdAt = this.createdAt,
        description = this.description,
        groupId = this.groupId,
        groupName = this.groupName,
        groupOwnerName = this.groupOwnerName,
        groupProfileUrl = this.groupProfileUrl,
    )
}