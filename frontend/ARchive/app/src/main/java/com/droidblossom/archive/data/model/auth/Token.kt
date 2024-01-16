package com.droidblossom.archive.data.model.auth

import com.droidblossom.archive.domain.dto.auth.TokenDto

data class Token(
    val accessToken: String,
    val expiresIn: String,
    val refreshToken: String,
    val refreshTokenExpiresIn: String
) {
    fun toDto() = TokenDto(
        accessToken = this.accessToken,
        expiresIn = this.expiresIn,
        refreshToken = this.refreshToken,
        refreshTokenExpiresIn = this.refreshTokenExpiresIn
    )
}