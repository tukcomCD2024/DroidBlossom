package com.droidblossom.archive.data.dto.group.response

import com.droidblossom.archive.domain.model.group.GroupPage

data class GroupPageResponseDto(
    val groups: List<GroupSummaryResponseDto>,
    val hasNext: Boolean
) {
    fun toModel() = GroupPage (
        groups = this.groups.map { it.toModel() },
        hasNext = this.hasNext,
    )
}