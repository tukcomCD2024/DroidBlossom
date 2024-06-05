package com.droidblossom.archive.data.dto.group.response

import com.droidblossom.archive.domain.model.group.GroupInvitedUsers

data class GroupInvitedUserListResponseDto (
    val response: List<GroupInvitedUserResponseDto>
){
    fun toModel() = GroupInvitedUsers(
        response = this.response.map { it.toModel() }
    )
}