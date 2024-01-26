package com.droidblossom.archive.domain.model.secret

import com.droidblossom.archive.data.dto.secret.request.SecretCapsulePageRequestDto

//변경될수도..? (1/26 기준)
data class SecretCapsulePageRequest(
    val size : Int,
    val capsule_id: Int
){
    fun toDto()= SecretCapsulePageRequestDto(
        size = this.size,
        capsule_id = this.capsule_id
    )
}

