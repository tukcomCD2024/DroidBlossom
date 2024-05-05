package com.droidblossom.archive.data.dto.secret.response

import com.droidblossom.archive.domain.model.secret.CapsulePageList

data class SecretCapsulePageResponseDto(
    val capsules: List<SecretCapsuleResponseDto>,
    val hasNext: Boolean,
    val hasPrevious: Boolean
){
    fun toModel() = CapsulePageList(
        capsules = this.capsules.map { it.toUIModel() },
        hasNext = this.hasNext,
        hasPrevious = this.hasPrevious,
    )
}