package com.droidblossom.archive.domain.model.member

data class MemberDetail(
    val nickname : String,
    val profileUrl : String,
    val tag : String,
    val friendCount: Int,
    val groupCount: Int
)