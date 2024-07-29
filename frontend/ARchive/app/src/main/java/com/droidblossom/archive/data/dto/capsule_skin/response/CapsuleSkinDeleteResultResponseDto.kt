package com.droidblossom.archive.data.dto.capsule_skin.response

import com.droidblossom.archive.domain.model.capsule_skin.CapsuleSkinDeleteResultResponse

data class CapsuleSkinDeleteResultResponseDto(
    val capsuleSkinDeleteResult: String,
    val message: String
) {
    fun toModel() = CapsuleSkinDeleteResultResponse(
        capsuleSkinDeleteResult = this.capsuleSkinDeleteResult,
        message = this.message
    )
}