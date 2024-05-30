package com.droidblossom.archive.domain.model.group

data class GroupInviteSummary(
    val createdAt: String,
    val description: String,
    val groupId: Int,
    val groupName: String,
    val groupOwnerName: String,
    val groupProfileUrl: String
)
