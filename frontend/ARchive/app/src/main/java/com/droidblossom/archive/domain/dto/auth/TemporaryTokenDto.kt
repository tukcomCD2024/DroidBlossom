package com.droidblossom.archive.domain.dto.auth

data class TemporaryTokenDto(
    val expiresIn: String,
    val temporaryAccessToken: String
)
