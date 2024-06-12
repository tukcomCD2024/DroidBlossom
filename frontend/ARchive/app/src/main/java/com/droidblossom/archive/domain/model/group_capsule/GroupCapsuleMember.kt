package com.droidblossom.archive.domain.model.group_capsule

data class GroupCapsuleMember(
    val memberId: Long,
    val nickname : String,
    val profileUrl : String,
    val isOpened : Boolean,
)
