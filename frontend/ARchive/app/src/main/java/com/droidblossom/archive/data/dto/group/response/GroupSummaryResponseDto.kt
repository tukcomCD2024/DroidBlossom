package com.droidblossom.archive.data.dto.group.response

import com.droidblossom.archive.domain.model.group.GroupSummary

data class GroupSummaryResponseDto(
    val createdAt: String,
    val groupOwnerProfileUrl: String,
    val totalGroupMemberCount : Int,
    val id: Long,
    val isOwner: Boolean,
    val name: String,
    val profileUrl: String
) {
    fun toModel() = GroupSummary(
        createdAt = this.createdAt,
        totalGroupMemberCount = this.totalGroupMemberCount,
        groupOwnerProfileUrl = this.groupOwnerProfileUrl,
        id = this.id,
        isOwner = this.isOwner,
        name = this.name,
        profileUrl = this.profileUrl
    )
}