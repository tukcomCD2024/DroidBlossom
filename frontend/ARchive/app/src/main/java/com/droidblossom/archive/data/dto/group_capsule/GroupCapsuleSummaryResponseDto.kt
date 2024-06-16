package com.droidblossom.archive.data.dto.group_capsule

import com.droidblossom.archive.domain.model.group_capsule.GroupCapsuleSummaryResponse


data class GroupCapsuleSummaryResponseDto(
    val groupId: Long,
    val groupMembers : List<GroupCapsuleMemberDto>,
    val groupName: String,
    val groupProfileUrl: String,
    val creatorNickname: String,
    val creatorProfileUrl: String,
    val skinUrl: String,
    val title: String,
    val dueDate: String?,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val roadName: String?,
    val isCapsuleOpened: Boolean,
    val isRequestMemberCapsuleOpened: Boolean,
    val hasEditPermission: Boolean,
    val hasDeletePermission: Boolean,
    val createdAt: String
){
    fun toModel() = GroupCapsuleSummaryResponse(
        groupId = this.groupId,
        groupMembers = this.groupMembers.map { it.toModel() },
        groupName = this.groupName,
        groupProfileUrl = this.groupProfileUrl,
        creatorNickname = this.creatorNickname,
        creatorProfileUrl = this.creatorProfileUrl,
        skinUrl = this.skinUrl,
        title = this.title,
        dueDate = this.dueDate ?: "",
        latitude = this.latitude,
        longitude = this.longitude,
        address = this.address,
        roadName = this.roadName ?: "",
        isCapsuleOpened = this.isCapsuleOpened,
        isRequestMemberCapsuleOpened = this.isRequestMemberCapsuleOpened,
        hasEditPermission = this.hasEditPermission,
        hasDeletePermission = this.hasDeletePermission,
        createdAt = this.createdAt
    )
}