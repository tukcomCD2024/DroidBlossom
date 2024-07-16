package com.droidblossom.archive.data.dto.group_capsule

import com.droidblossom.archive.domain.model.common.SocialCapsules

data class GroupCapsuleResponseDto(
    val capsuleId: Long,
    val groupId: Long,
    val groupName: String,
    val groupProfileUrl: String,
    val address: String,
    val roadName: String,
    val capsuleSkinUrl: String,
    val content: String?,
    val createdAt: String,
    val creatorProfileUrl: String,
    val dueDate: String?,
    val isOpened: Boolean,
    val imageUrls: List<String>?,
    val videoUrls: List<String>?,
    val creatorNickname: String,
    val title: String,
    val capsuleType: String,
){
    fun toModel()= SocialCapsules(
        capsuleId = this.capsuleId,
        title = this.title,
        content = this.content ?: "",
        createdDate = this.createdAt,
        dueDate = this.dueDate,
        profileUrl = this.groupProfileUrl,
        capsuleSkinUrl = this.capsuleSkinUrl,
        nickNameOrGroupName = this.groupName,
        roadName = this.roadName,
        address = this.address,
        hasThumbnail = !(imageUrls.isNullOrEmpty() && videoUrls.isNullOrEmpty()),
        thumbnailImage = imageUrls?.firstOrNull() ?: videoUrls?.firstOrNull() ?: "",
        isOpened = this.isOpened
    )
}
