package com.droidblossom.archive.data.dto.group.response

import com.droidblossom.archive.domain.model.group.GroupMembersInfo

data class GroupMembersInfoResponseDto (
    val groupMemberResponses : List<GroupMemberResponseDto>
){
    fun toModel() = GroupMembersInfo(
        groupMemberResponses = this.groupMemberResponses.map { it.toGroupMemberListModel() }
    )
}