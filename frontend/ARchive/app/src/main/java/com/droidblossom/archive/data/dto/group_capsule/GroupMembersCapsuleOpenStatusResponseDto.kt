package com.droidblossom.archive.data.dto.group_capsule

import com.droidblossom.archive.domain.model.group_capsule.GroupMembersCapsuleOpenStatusResponse

data class GroupMembersCapsuleOpenStatusResponseDto(
    val groupMemberCapsuleOpenStatus: List<GroupCapsuleMemberDto>
) {
    fun toModel() = GroupMembersCapsuleOpenStatusResponse(
        groupMemberCapsuleOpenStatus = this.groupMemberCapsuleOpenStatus.map { it.toModel() }
    )
}
