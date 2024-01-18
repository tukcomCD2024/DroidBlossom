package com.droidblossom.archive.data.dto.auth.response

import com.droidblossom.archive.domain.model.auth.Token

data class TokenResponseDto(
    val accessToken: String,
    val expiresIn: String,
    val refreshToken: String,
    val refreshTokenExpiresIn: String
) {
    fun toModel() = Token(
        accessToken = this.accessToken,
        expiresIn = this.expiresIn,
        refreshToken = this.refreshToken,
        refreshTokenExpiresIn = this.refreshTokenExpiresIn
    )
}