package com.droidblossom.archive.domain.model.group

data class GroupInvitedUsersPage(
    val groupSendingInviteMembers: List<GroupInvitedUser>,
    val hasNext: Boolean
)
