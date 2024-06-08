package com.droidblossom.archive.data.dto.group_capsule

import com.droidblossom.archive.domain.model.group_capsule.GroupCapsuleSummaryResponse


data class GroupCapsuleSummaryResponseDto(
    val members : List<GroupCapsuleMemberDto>,
    val nickname: String,
    val profileUrl: String,
    val skinUrl: String,
    val title: String,
    val dueDate: String?,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val roadName: String?,
    val isOpened: Boolean,
    val createdAt: String
){
    fun toModel() = GroupCapsuleSummaryResponse(
        members = this.members.map { it.toModel() },
        nickname = this.nickname,
        profileUrl = this.profileUrl,
        skinUrl = this.skinUrl,
        title = this.title,
        dueDate = this.dueDate ?: "",
        latitude = this.latitude,
        longitude = this.longitude,
        address = this.address,
        roadName = this.roadName ?: "",
        isOpened = this.isOpened,
        createdAt = this.createdAt

    )
}