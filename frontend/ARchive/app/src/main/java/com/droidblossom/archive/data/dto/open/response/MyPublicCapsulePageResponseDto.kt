package com.droidblossom.archive.data.dto.open.response

import com.droidblossom.archive.data.dto.common.CapsuleBasicInfoResponseDto
import com.droidblossom.archive.domain.model.secret.CapsulePageList

data class MyPublicCapsulePageResponseDto (
    val publicCapsules: List<CapsuleBasicInfoResponseDto>,
    val hasNext: Boolean,
){
    fun toModel() = CapsulePageList(
        capsules = this.publicCapsules.map { it.toUIModel() },
        hasNext = this.hasNext,
    )
}