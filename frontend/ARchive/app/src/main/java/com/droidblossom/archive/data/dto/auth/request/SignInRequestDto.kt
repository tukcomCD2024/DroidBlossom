package com.droidblossom.archive.data.dto.auth.request

import java.io.Serializable

data class SignInRequestDto(
    val authId: String,
    val socialType: String
) : Serializable
