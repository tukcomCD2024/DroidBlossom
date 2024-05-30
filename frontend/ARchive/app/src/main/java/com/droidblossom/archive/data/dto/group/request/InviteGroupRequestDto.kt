package com.droidblossom.archive.data.dto.group.request

data class InviteGroupRequestDto (
    val groupId : Long,
    val targetIds : List<Long>
)