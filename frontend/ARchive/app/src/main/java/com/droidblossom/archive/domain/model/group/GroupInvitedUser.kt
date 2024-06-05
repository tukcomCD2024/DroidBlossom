package com.droidblossom.archive.domain.model.group

data class GroupInvitedUser(
    val id: Long,
    val nickname: String,
    val profileUrl: String,
    val sendingInvitesCreatedAt: String,
)
