package com.droidblossom.archive.data.dto.auth.request

data class SignUpRequestDto(
    val authId: String,
    val email: String,
    val profileUrl: String,
    val socialType: String
)