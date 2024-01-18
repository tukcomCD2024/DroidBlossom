package com.droidblossom.archive.domain.model.member

import com.droidblossom.archive.data.dto.member.request.CheckStatusRequestDto
import com.droidblossom.archive.presentation.ui.auth.AuthViewModel
import com.droidblossom.archive.util.SocialUtils

data class CheckStatus (
    val authId : String,
    val socialType : AuthViewModel.Social
){
    fun toDto() = CheckStatusRequestDto(
        authId = this.authId,
        socialType = SocialUtils.enumToString(this.socialType)
    )
}