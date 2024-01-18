package com.droidblossom.archive.data.dto.member.request
import java.io.Serializable


data class MemberDetailUpdateRequestDto(
    val nickname : String,
    val profileImage : String
) : Serializable
