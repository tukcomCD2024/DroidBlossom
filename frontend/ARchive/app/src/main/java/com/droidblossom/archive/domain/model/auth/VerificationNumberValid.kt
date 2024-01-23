package com.droidblossom.archive.domain.model.auth

import com.droidblossom.archive.data.dto.auth.request.VerificationNumberValidRequestDto

data class VerificationNumberValid(
    val certificationNumber : String,
    val receiver : String
){
    fun toDto() = VerificationNumberValidRequestDto(
        certificationNumber = this.certificationNumber,
        receiver = this.receiver
    )
}
