package com.droidblossom.archive.data.dto.auth.request

import java.io.Serializable

data class VerificationMessageSendRequestDto(
    val receiver : String,
    val appHashKey : String
) : Serializable
