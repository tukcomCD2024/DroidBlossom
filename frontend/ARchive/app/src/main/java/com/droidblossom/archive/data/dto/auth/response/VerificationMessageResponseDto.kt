package com.droidblossom.archive.data.dto.auth.response

import java.io.Serializable

data class VerificationMessageResponseDto (
    val status : String,
    val message : String,
    val field : String,
    val value : String,
    val reason : String
) : Serializable