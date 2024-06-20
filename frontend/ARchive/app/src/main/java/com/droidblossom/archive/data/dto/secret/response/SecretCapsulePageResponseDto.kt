package com.droidblossom.archive.data.dto.secret.response

import com.droidblossom.archive.data.dto.common.CapsuleBasicInfoResponseDto
import com.droidblossom.archive.domain.model.secret.CapsulePageList

data class SecretCapsulePageResponseDto(
    val capsules: List<CapsuleBasicInfoResponseDto>,
    val hasNext: Boolean,
){
    fun toModel() = CapsulePageList(
        capsules = this.capsules.map { it.toUIModel() },
        hasNext = this.hasNext
    )
}