package com.droidblossom.archive.data.dto.member.request

import java.io.Serializable

data class CheckStatusRequestDto(
    val authId : String,
    val socialType : String
) : Serializable
