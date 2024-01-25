package com.droidblossom.archive.domain.model.auth

data class TemporaryToken(
    val expiresIn: String,
    val temporaryAccessToken: String
)
