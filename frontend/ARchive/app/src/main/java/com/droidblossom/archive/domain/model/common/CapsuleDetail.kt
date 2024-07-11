package com.droidblossom.archive.domain.model.common

import com.droidblossom.archive.domain.model.group_capsule.GroupCapsuleMember

// 모든 캡슐 디테일로 통합될 예정 maybe
data class CapsuleDetail(
    val address: String,
    val roadName: String,
    val capsuleSkinUrl: String,
    val content: String,
    val createdDate: String,
    val dueDate: String?,
    val members: List<GroupCapsuleMember> = listOf(),
    val profileUrl: String,
    val isOpened: Boolean,
    val imageUrls: List<String>?,
    val videoUrls: List<String>?,
    val nickname: String,
    val title: String,
    val capsuleType: String,
    val isOwner: Boolean
) {
    constructor() : this(
        "",
        "",
        "",
        "",
        "",
        null,
        listOf(),
        "",
        false,
        listOf(),
        listOf(),
        "",
        "",
        "",
        isOwner = true
    )
}
