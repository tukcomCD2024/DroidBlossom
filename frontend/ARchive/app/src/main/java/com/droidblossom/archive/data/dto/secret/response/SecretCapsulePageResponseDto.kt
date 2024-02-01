package com.droidblossom.archive.data.dto.secret.response

import com.droidblossom.archive.domain.model.secret.SecretCapsulePage

data class SecretCapsulePageResponseDto(
    val capsules: List<SecretCapsuleSummeryResponseDto>,
    val hasNext: Boolean,
    val hasPrevious: Boolean
){
    fun toModel() = SecretCapsulePage(
        capsules = this.capsules.map { it.toModel() },
        hasNext = this.hasNext,
        hasPrevious = this.hasPrevious,
    )
}