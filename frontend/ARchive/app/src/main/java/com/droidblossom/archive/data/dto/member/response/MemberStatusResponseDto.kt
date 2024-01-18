package com.droidblossom.archive.data.dto.member.response

import com.droidblossom.archive.domain.model.member.MemberStatus
import java.io.Serializable

data class MemberStatusResponseDto(
    val isExist : Boolean,
    val isVerified : Boolean
) : Serializable {
    fun toModel() = MemberStatus(
        isExist = this.isExist,
        isVerified = this.isExist
    )
}
