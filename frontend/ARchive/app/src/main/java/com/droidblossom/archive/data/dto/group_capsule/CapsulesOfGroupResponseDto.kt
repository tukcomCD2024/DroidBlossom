package com.droidblossom.archive.data.dto.group_capsule

import com.droidblossom.archive.data.dto.common.CapsuleBasicInfoResponseDto
import com.droidblossom.archive.domain.model.secret.CapsulePageList

data class CapsulesOfGroupResponseDto(
    val capsuleBasicInfos : List<CapsuleBasicInfoResponseDto>,
    val hasNext: Boolean
){
    fun toModel() = CapsulePageList(
        capsules = this.capsuleBasicInfos.map { it.toUIModel() },
        hasNext = this.hasNext,
    )
}
