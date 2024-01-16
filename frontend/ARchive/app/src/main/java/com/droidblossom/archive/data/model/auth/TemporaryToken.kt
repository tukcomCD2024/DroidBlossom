package com.droidblossom.archive.data.model.auth

data class TemporaryToken(
    val expiresIn: String,
    val temporaryAccessToken: String
)