package com.droidblossom.archive.domain.model.auth

import com.droidblossom.archive.data.dto.auth.request.SignUpRequestDto
import com.droidblossom.archive.presentation.ui.auth.AuthViewModel
import com.droidblossom.archive.util.SocialUtils

data class SignUp(
    val authId: String,
    val email: String,
    val profileUrl: String,
    val socialType: AuthViewModel.Social
){
    fun toDto() = SignUpRequestDto(
        authId = this.authId,
        email = this.email,
        profileUrl = this.profileUrl,
        socialType = SocialUtils.enumToString(this.socialType)
    )
}
