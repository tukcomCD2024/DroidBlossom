package com.droidblossom.archive.domain.model.member

data class NotificationPage(
    val hasNext: Boolean,
    val notifications: List<NotificationModel>
)
