package com.droidblossom.archive.domain.model.capsule_skin

import com.droidblossom.archive.data.dto.capsule_skin.request.CapsuleSkinsSearchPageRequestDto

data class CapsuleSkinsSearchPageRequest(
    val capsule_skin_name : Int,
    val size : Int,
    val capsule_skin_id : Int
){
    fun toDto()= CapsuleSkinsSearchPageRequestDto(
        capsule_skin_name = this.capsule_skin_name,
        size = this.size,
        capsule_skin_id = this.capsule_skin_id
    )
}
