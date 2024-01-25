package com.droidblossom.archive.data.dto.member.request

import java.io.Serializable

data class MemberStatusRequestDto(
    val authId : String,
    val socialType : String
) : Serializable
