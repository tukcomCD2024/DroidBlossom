package com.droidblossom.archive.domain.model.common

data class MyCapsule (
    val capsuleId: Long,
    val capsuleSkinUrl: String,
    val createdDate: String,
    val dueDate: String?,
    val isOpened: Boolean,
    val title: String,
    val capsuleType: String
)