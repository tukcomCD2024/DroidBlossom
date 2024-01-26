package com.droidblossom.archive.domain.model.secret

import com.droidblossom.archive.data.dto.secret.request.SecretCapsulePageRequestDto

data class SecretCapsulePageRequest(
    val size : Int,
    val capsule_id: Int
){
    fun toDto()= SecretCapsulePageRequestDto(
        size = this.size,
        capsule_id = this.capsule_id
    )
}

