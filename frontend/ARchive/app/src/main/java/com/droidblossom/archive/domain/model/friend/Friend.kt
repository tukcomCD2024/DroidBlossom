package com.droidblossom.archive.domain.model.friend

data class Friend(
    val createdAt: String,
    val id: Long,
    val nickname: String,
    val profileUrl: String,
    var isOpenDelete : Boolean,
)