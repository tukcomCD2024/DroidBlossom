package com.droidblossom.archive.domain.model.capsule_skin

import com.droidblossom.archive.data.dto.capsule_skin.request.CapsuleSkinsPageRequestDto

data class CapsuleSkinsPageRequest(
    val size : Int,
    val createdAt: String
){
    fun toDto()= CapsuleSkinsPageRequestDto(
        size = this.size,
        createdAt = this.createdAt
    )
}

