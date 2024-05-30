package com.droidblossom.archive.domain.model.group

data class GroupInviteSummary(
    val createdAt: String,
    val description: String,
    val groupId: Long,
    val groupName: String,
    val groupOwnerName: String,
    val groupProfileUrl: String
)
