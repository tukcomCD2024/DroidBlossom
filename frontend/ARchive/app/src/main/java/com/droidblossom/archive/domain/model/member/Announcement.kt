package com.droidblossom.archive.domain.model.member

import com.droidblossom.archive.domain.model.setting.NoticeContent

data class Announcement(
    val content: NoticeContent,
    val createdAt: String,
    val title: String,
    val version: String,
    var isOpen : Boolean = false,
)