package com.droidblossom.archive.data.dto.auth.response

import com.droidblossom.archive.domain.model.auth.VerificationMessageResult
import java.io.Serializable

data class VerificationMessageResponseDto(
    val status: String,
    val message: String,
    val field: String,
    val value: String,
    val reason: String
) : Serializable {
    fun toModel() = VerificationMessageResult(
        status = this.status,
        message = this.message,
        field = this.field,
        value = this.value,
        reason = this.reason
    )
}