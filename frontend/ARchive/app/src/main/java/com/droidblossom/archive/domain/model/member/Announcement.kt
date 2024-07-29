package com.droidblossom.archive.domain.model.member

data class Announcement(
    val content: List<String>,
    val createdAt: String,
    val title: String,
    val version: String
)