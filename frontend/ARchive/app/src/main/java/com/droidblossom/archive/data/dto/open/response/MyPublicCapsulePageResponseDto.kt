package com.droidblossom.archive.data.dto.open.response

import com.droidblossom.archive.data.dto.secret.response.SecretCapsuleResponseDto
import com.droidblossom.archive.domain.model.secret.CapsulePageList

data class MyPublicCapsulePageResponseDto (
    val publicCapsules: List<SecretCapsuleResponseDto>,
    val hasNext: Boolean,
){
    fun toModel() = CapsulePageList(
        capsules = this.publicCapsules.map { it.toUIModel() },
        hasNext = this.hasNext,
        hasPrevious = this.hasNext,
    )
}