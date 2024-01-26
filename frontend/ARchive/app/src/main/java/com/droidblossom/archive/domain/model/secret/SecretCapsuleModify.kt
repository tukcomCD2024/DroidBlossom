package com.droidblossom.archive.domain.model.secret

data class SecretCapsuleModify(
    val address: String,
    val dueDate: String,
    val id: Int,
    val isOpened: Boolean,
    val nickname: String,
    val skinUrl: String,
    val title: String
)
