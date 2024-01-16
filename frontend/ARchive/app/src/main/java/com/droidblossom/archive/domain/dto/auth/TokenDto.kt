package com.droidblossom.archive.domain.dto.auth

data class TokenDto(
    val accessToken: String,
    val expiresIn: String,
    val refreshToken: String,
    val refreshTokenExpiresIn: String
)
