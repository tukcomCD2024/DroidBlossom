package com.droidblossom.archive.data.dto.member.response

import com.droidblossom.archive.domain.model.member.NotificationPage

data class NotificationResponseDto(
    val hasNext: Boolean,
    val responseList: List<NoficationListResponseDto>
){
    fun toModel() = NotificationPage(
        hasNext = this.hasNext,
        notifications = this.responseList.map { it.toModel() }
    )
}