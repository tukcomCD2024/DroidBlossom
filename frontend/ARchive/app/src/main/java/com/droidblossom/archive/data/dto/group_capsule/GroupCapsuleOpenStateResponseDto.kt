package com.droidblossom.archive.data.dto.group_capsule

import com.droidblossom.archive.domain.model.group_capsule.GroupCapsuleOpenState
import com.droidblossom.archive.domain.model.group_capsule.GroupCapsuleOpenStateResponse

data class GroupCapsuleOpenStateResponseDto(
    val capsuleOpenStatus: String,
    val statusMessage: String
){
    fun toModel() = GroupCapsuleOpenStateResponse(
        capsuleOpenStatus = GroupCapsuleOpenState.valueOf(this.capsuleOpenStatus),
        statusMessage = this.statusMessage
    )
}
