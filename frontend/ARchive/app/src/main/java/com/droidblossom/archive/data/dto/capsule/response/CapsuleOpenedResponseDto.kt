package com.droidblossom.archive.data.dto.capsule.response

import com.droidblossom.archive.domain.model.capsule.CapsuleOpenedResponse

data class CapsuleOpenedResponseDto(
    val result: String,
){
    fun toModel() = CapsuleOpenedResponse(
        result = this.result
    )
}
