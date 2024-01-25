package com.droidblossom.archive.domain.model.auth

import com.droidblossom.archive.data.dto.auth.request.TokenReIssueRequestDto

data class TokenReIssue(
    val refreshToken: String
){
    fun toDto() = TokenReIssueRequestDto(
        refreshToken = this.refreshToken
    )
}
