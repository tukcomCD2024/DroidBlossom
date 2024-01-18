package com.droidblossom.archive.data.dto.auth.response

import com.droidblossom.archive.domain.model.auth.OAuthUrl

data class OAuthUrlResponseDto(
    val url: String
){
    fun toModel() = OAuthUrl(
        url = this.url
    )
}