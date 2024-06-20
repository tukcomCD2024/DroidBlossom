package com.droidblossom.archive.domain.model.group

data class GroupSummary(
    val createdAt: String,
    val id: Long,
    val isOwner: Boolean,
    val name: String,
    val profileUrl: String,
    val groupOwnerProfileUrl: String,
    val totalGroupMemberCount : Int,
    var isChecked : Boolean = false,
)