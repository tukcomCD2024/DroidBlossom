package com.droidblossom.archive.domain.model.secret

data class SecretCapsuleDetail(
    val address: String,
    val capsuleSkinUrl: String,
    val content: String,
    val createdDate: String,
    val dueDate: String,
    val isOpened: Boolean,
    val mediaUrls: List<String>,
    val nickname: String,
    val title: String
)
