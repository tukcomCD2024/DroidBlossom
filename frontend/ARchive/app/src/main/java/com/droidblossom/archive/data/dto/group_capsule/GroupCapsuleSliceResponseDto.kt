package com.droidblossom.archive.data.dto.group_capsule

import com.droidblossom.archive.domain.model.group_capsule.GroupCapsuleSliceResponse

data class GroupCapsuleSliceResponseDto(
    val groupCapsuleResponses: List<GroupCapsuleResponseDto>,
    val hasNext: Boolean
){
    fun toModel()= GroupCapsuleSliceResponse(
        groupCapsules = this.groupCapsuleResponses.map { it.toModel() },
        hasNext = this.hasNext
    )
}
