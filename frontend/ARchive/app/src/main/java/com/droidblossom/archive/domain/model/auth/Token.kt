package com.droidblossom.archive.domain.model.auth

data class
Token(
    val accessToken: String,
    val expiresIn: String,
    val refreshToken: String,
    val refreshTokenExpiresIn: String
)
