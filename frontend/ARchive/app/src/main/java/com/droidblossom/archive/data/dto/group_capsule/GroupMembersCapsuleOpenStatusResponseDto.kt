package com.droidblossom.archive.data.dto.group_capsule

import com.droidblossom.archive.domain.model.group_capsule.GroupMembersCapsuleOpenStatusResponse

data class GroupMembersCapsuleOpenStatusResponseDto(
    val groupCapsuleMembers: List<GroupCapsuleMemberDto>
) {
    fun toModel() = GroupMembersCapsuleOpenStatusResponse(
        groupMemberCapsuleOpenStatus = this.groupCapsuleMembers.map { it.toModel() }
    )
}
