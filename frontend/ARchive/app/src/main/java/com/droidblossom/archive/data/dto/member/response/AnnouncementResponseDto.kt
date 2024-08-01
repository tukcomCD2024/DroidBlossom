package com.droidblossom.archive.data.dto.member.response

import com.droidblossom.archive.domain.model.member.Announcement
import com.droidblossom.archive.domain.model.setting.NoticeContent

data class AnnouncementResponseDto(
    val content: String,
    val createdAt: String,
    val title: String,
    val version: String
) {
    fun toModel() = Announcement(
        content = NoticeContent(content),
        createdAt = createdAt,
        title = title,
        version = version,
    )
}