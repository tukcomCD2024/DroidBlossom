package com.droidblossom.archive.data.dto.auth.request

import java.io.Serializable

data class VerificationNumberValidDto(
    val certificationNumber : String,
    val receiver : String
) : Serializable
