package com.droidblossom.archive.domain.model.secret

data class SecretCapsuleSummary(
    val nickname: String,
    val profileUrl: String,
    val skinUrl: String,
    val title: String,
    val dueDate: String,
    val address: String,
    val roadName: String,
    val isOpened: Boolean,
    val createdAt: String
)
