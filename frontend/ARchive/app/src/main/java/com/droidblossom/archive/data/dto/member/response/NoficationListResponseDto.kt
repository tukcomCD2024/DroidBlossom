package com.droidblossom.archive.data.dto.member.response

import com.droidblossom.archive.domain.model.member.NotiCategoryName
import com.droidblossom.archive.domain.model.member.NotificationModel

data class NoficationListResponseDto(
    val createdAt: String,
    val imageUrl: String,
    val text: String,
    val title: String,
    val categoryName: String,
    val status: String?
) {
    fun toModel() = NotificationModel(
        createdAt = this.createdAt,
        imageUrl = this.imageUrl,
        text = this.text,
        title = this.title,
        categoryName = NotiCategoryName.valueOf(categoryName),
        status = this.status ?: "",
    )
}