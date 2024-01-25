package com.droidblossom.archive.domain.model.auth

import com.droidblossom.archive.data.dto.auth.request.VerificationMessageSendRequestDto

data class VerificationMessageSend (
    val receiver : String,
    val appHashKey : String
){
    fun toDto() = VerificationMessageSendRequestDto(
        receiver = this.receiver,
        appHashKey = this.appHashKey
    )
}