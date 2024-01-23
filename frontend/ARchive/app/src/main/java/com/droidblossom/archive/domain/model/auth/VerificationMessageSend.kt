package com.droidblossom.archive.domain.model.auth

import com.droidblossom.archive.data.dto.auth.request.VerificationMessageSendDto

data class VerificationMessageSend (
    val receiver : String,
    val appHashKey : String
){
    fun toDto() = VerificationMessageSendDto(
        receiver = this.receiver,
        appHashKey = this.appHashKey
    )
}