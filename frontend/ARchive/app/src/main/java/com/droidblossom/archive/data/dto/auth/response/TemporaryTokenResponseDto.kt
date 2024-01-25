package com.droidblossom.archive.data.dto.auth.response

import com.droidblossom.archive.domain.model.auth.TemporaryToken

data class TemporaryTokenResponseDto(
    val expiresIn: String,
    val temporaryAccessToken: String
) {
    fun toModel() = TemporaryToken(
        expiresIn = this.expiresIn,
        temporaryAccessToken = this.temporaryAccessToken
    )
}