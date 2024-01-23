package com.droidblossom.archive.data.dto.auth.request

import java.io.Serializable

data class VerificationMessageSendDto(
    val receiver : String,
    val appHashKey : String
) : Serializable
