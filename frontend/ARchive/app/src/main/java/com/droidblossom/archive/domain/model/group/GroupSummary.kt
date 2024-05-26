package com.droidblossom.archive.domain.model.group

data class GroupSummary(
    val createdAt: String,
    val description: String,
    val id: Long,
    val isOwner: Boolean,
    val name: String,
    val profileUrl: String,
    var isChecked : Boolean = false,
)