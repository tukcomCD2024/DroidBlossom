package com.droidblossom.archive.data.dto.group.response

import com.droidblossom.archive.domain.model.group.GroupDetail

data class GroupDetailResponseDto(
    val groupName: String,
    val groupProfileUrl: String,
    val groupDescription: String,
    val createdAt: String,
    val members: List<GroupMemberResponseDto>,
){
    fun toModel() = GroupDetail(
        groupName = this.groupName,
        groupProfileUrl = this.groupProfileUrl,
        groupDescription = this.groupDescription,
        createdAt = this.createdAt,
        members = this.members.map { it.toModel() }
    )
}
