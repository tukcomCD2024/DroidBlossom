package com.droidblossom.archive.domain.model.auth

import com.droidblossom.archive.data.dto.auth.request.VerificationNumberValidDto

data class VerificationNumberValid(
    val certificationNumber : String,
    val receiver : String
){
    fun toDto() = VerificationNumberValidDto(
        certificationNumber = this.certificationNumber,
        receiver = this.receiver
    )
}
