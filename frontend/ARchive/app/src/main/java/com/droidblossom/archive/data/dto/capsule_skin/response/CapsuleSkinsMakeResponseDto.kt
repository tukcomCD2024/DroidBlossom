package com.droidblossom.archive.data.dto.capsule_skin.response

import com.droidblossom.archive.domain.model.capsule_skin.CapsuleSkinsMakeResponse

data class CapsuleSkinsMakeResponseDto(
    val status : String,
    val result : String
){
    fun toModel() = CapsuleSkinsMakeResponse(
        status = this.status,
        result = this.result
    )
}
