package com.droidblossom.archive.domain.model.group

data class GroupDetail (
    val groupName: String,
    val groupProfileUrl: String,
    val groupDescription: String,
    val createdAt: String,
    val members: List<GroupMember>,
)