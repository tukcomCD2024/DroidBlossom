package com.droidblossom.archive.domain.model.auth

import com.droidblossom.archive.data.dto.auth.request.SignInRequestDto
import com.droidblossom.archive.data.dto.auth.request.SignUpRequestDto
import com.droidblossom.archive.presentation.ui.auth.AuthViewModel
import com.droidblossom.archive.util.SocialUtils

data class SignIn (
    val authId: String,
    val socialType: AuthViewModel.Social
){
    fun toDto() = SignInRequestDto(
        authId = this.authId,
        socialType = SocialUtils.enumToString(this.socialType)
    )
}
