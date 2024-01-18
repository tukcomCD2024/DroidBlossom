package com.droidblossom.archive.domain.model.auth

import com.droidblossom.archive.data.dto.auth.request.SignUpRequestDto

data class SignUp(
    val authId: String,
    val email: String,
    val profileUrl: String,
    val socialType: String
){
    fun toDto() = SignUpRequestDto(
        authId = this.authId,
        email = this.email,
        profileUrl = this.profileUrl,
        socialType = this.socialType
    )
}
