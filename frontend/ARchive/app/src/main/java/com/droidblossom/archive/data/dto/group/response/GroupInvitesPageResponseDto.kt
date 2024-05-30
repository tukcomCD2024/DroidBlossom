package com.droidblossom.archive.data.dto.group.response

import com.droidblossom.archive.domain.model.group.GroupPage

data class GroupInvitesPageResponseDto(
    val hasNext: Boolean,
    val responses: List<GroupInviteResponseDto>
) {
    fun toModel() = GroupPage(
        groups = this.responses.map { it.toModel() },
        hasNext = this.hasNext
    )
}