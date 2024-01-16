package com.droidblossom.archive.data.model.auth

import com.droidblossom.archive.domain.dto.auth.OAuthUrlDto

data class OAuthUrl(
    val url: String
){
    fun toDto() = OAuthUrlDto(
        url = this.url
    )
}