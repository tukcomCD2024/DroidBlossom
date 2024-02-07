package com.droidblossom.archive.data.dto.secret.request

data class SecretCapsuleModifyRequestDto(
    val title: String,
    val content: String,
    val media: List<String>,
    val dueData: String,
    val capsuleSkinId: Long
)
