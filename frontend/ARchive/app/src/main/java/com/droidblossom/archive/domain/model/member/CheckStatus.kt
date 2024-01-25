package com.droidblossom.archive.domain.model.member

import com.droidblossom.archive.data.dto.member.request.MemberStatusRequestDto
import com.droidblossom.archive.presentation.ui.auth.AuthViewModel
import com.droidblossom.archive.util.SocialUtils

data class CheckStatus (
    val authId : String,
    val socialType : AuthViewModel.Social
){
    fun toDto() = MemberStatusRequestDto(
        authId = this.authId,
        socialType = SocialUtils.enumToString(this.socialType)
    )
}