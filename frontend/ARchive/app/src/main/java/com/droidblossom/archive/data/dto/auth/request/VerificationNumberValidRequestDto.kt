package com.droidblossom.archive.data.dto.auth.request

import java.io.Serializable

data class VerificationNumberValidRequestDto(
    val certificationNumber : String,
    val receiver : String
) : Serializable
