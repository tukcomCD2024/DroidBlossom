package com.droidblossom.archive.domain.model.group

data class GroupInvitedUser(
    val groupInviteId: Long,
    val memberId: Long,
    val nickname: String,
    val profileUrl: String,
    val sendingInvitesCreatedAt: String
)
