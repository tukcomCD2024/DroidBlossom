package com.droidblossom.archive.data.dto.member.response

import com.droidblossom.archive.domain.model.member.Announcements

data class AnnouncementsResponseDto(
    val announcements: List<AnnouncementResponseDto>
) {
    fun toModel() = Announcements(
        announcements = announcements.map { it.toModel() }
    )
}