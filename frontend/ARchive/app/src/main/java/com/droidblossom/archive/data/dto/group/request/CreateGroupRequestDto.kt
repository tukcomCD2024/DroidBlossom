package com.droidblossom.archive.data.dto.group.request

data class CreateGroupRequestDto(
    val description: String,
    val groupDirectory: String,
    val groupImage: String,
    val groupName: String,
    val targetIds: List<Int>
)