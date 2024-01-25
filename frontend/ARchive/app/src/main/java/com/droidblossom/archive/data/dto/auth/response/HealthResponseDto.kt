package com.droidblossom.archive.data.dto.auth.response

import com.droidblossom.archive.domain.model.auth.Health

data class HealthResponseDto(
    val message : String
){
    fun toModel() = Health(
        message = this.message
    )
}