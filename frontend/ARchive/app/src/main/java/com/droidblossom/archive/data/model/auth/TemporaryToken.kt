package com.droidblossom.archive.data.model.auth

import com.droidblossom.archive.domain.dto.auth.TemporaryTokenDto

data class TemporaryToken(
    val expiresIn: String,
    val temporaryAccessToken: String
){
    fun toDto() = TemporaryTokenDto(
        expiresIn = this.expiresIn,
        temporaryAccessToken = this.temporaryAccessToken
    )
}