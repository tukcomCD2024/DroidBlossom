package com.droidblossom.archive.data.dto.group.response

import com.droidblossom.archive.data.dto.common.CapsuleBasicInfoResponseDto
import com.droidblossom.archive.domain.model.secret.CapsulePageList

data class MyGroupCapsulePageResponseDto(
    val groupCapsules: List<CapsuleBasicInfoResponseDto>,
    val hasNext: Boolean,
) {
    fun toModel() = CapsulePageList(
        capsules = this.groupCapsules.map { it.toUIModel() },
        hasNext = this.hasNext,
    )
}