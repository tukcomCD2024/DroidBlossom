package com.droidblossom.archive.domain.model.common

data class CapsuleSummaryResponse(
    val nicknameOrGroupName: String,
    val profileOrGroupProfileUrl: String,
    val skinUrl: String,
    val title: String,
    val dueDate: String,
    val address: String,
    val roadName: String,
    val isOpened: Boolean,
    val createdAt: String,
    val isOwner: Boolean
)
