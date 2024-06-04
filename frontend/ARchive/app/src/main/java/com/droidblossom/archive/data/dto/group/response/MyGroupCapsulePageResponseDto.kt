package com.droidblossom.archive.data.dto.group.response

import com.droidblossom.archive.data.dto.common.MyCapsuleResponseDto
import com.droidblossom.archive.domain.model.secret.CapsulePageList

data class MyGroupCapsulePageResponseDto(
    val groupCapsules: List<MyCapsuleResponseDto>,
    val hasNext: Boolean,
) {
    fun toModel() = CapsulePageList(
        capsules = this.groupCapsules.map { it.toUIModel() },
        hasNext = this.hasNext,
        hasPrevious = this.hasNext,
    )
}