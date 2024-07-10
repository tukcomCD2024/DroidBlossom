package com.droidblossom.archive.domain.model.member

data class MemberStatus(
    val isExist : Boolean,
    val isVerified : Boolean,
    val isDeleted: Boolean
)
