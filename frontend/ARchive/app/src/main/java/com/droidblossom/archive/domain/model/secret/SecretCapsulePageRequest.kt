package com.droidblossom.archive.domain.model.secret

import com.droidblossom.archive.data.dto.secret.request.SecretCapsulePageRequestDto

data class SecretCapsulePageRequest(
    val size : Int,
    val createdAt: String
){
    fun toDto()= SecretCapsulePageRequestDto(
        size = this.size,
        createdAt = this.createdAt
    )
}

