package com.droidblossom.archive.domain.model.group_capsule

import com.droidblossom.archive.domain.model.common.CapsuleSummaryResponse

data class GroupCapsuleSummaryResponse(
    val groupId: Long,
    val groupMembers : List<GroupCapsuleMember>,
    val groupName: String,
    val groupProfileUrl: String,
    val creatorNickname: String,
    val creatorProfileUrl: String,
    val skinUrl: String,
    val title: String,
    val dueDate: String,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val roadName: String,
    val isCapsuleOpened: Boolean,
    val isRequestMemberCapsuleOpened: Boolean,
    val hasEditPermission: Boolean,
    val hasDeletePermission: Boolean,
    val createdAt: String
){
    fun toCapsuleSummaryResponseModel() = CapsuleSummaryResponse(
        nicknameOrGroupName = this.groupName,
        profileOrGroupProfileUrl = this.groupProfileUrl,
        skinUrl = this.skinUrl,
        title = this.title,
        dueDate = this.dueDate,
        address = this.address,
        roadName = this.roadName,
        isOpened = this.isCapsuleOpened,
        createdAt = this.createdAt
    )
}

