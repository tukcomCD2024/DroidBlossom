package com.droidblossom.archive.data.dto.auth.response

import com.droidblossom.archive.domain.model.auth.VerificationMessageResult
import java.io.Serializable

data class VerificationMessageResponseDto(
    val status: String,
    val message: String,
) : Serializable {
    fun toModel() = VerificationMessageResult(
        status = this.status,
        message = this.message,
    )
}