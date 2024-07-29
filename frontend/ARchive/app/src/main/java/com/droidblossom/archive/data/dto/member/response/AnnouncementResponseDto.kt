package com.droidblossom.archive.data.dto.member.response

import com.droidblossom.archive.domain.model.member.Announcement

data class AnnouncementResponseDto(
    val content: String,
    val createdAt: String,
    val title: String,
    val version: String
) {
    fun toModel() = Announcement(
        content = listOf(content),
        createdAt = createdAt,
        title = title,
        version = version,
    )
}