package com.droidblossom.archive.domain.model.member

data class NotificationModel(
    val createdAt: String,
    val imageUrl: String,
    val text: String,
    val title: String,
    val categoryName : NotiCategoryName,
    val status : String
)
