package com.droidblossom.archive.presentation.model.mypage

data class CapsuleData (
    val capsuleId: Long,
    val capsuleSkinUrl: String,
    val createdDate: String,
    val dueDate: String?,
    var isOpened: Boolean,
    val title: String,
    val capsuleType: String
)