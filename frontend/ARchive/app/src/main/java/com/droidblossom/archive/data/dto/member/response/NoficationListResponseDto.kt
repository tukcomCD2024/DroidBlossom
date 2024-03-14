package com.droidblossom.archive.data.dto.member.response

import com.droidblossom.archive.domain.model.member.NotificationModel

data class NoficationListResponseDto(
    val createdAt: String,
    val imageUrl: String,
    val text: String,
    val title: String
) {
    fun toModel() = NotificationModel(
        createdAt = this.createdAt,
        imageUrl = this.imageUrl,
        text = this.text,
        title = this.title
    )
}