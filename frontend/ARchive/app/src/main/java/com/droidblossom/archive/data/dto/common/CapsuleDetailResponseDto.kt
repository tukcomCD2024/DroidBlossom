package com.droidblossom.archive.data.dto.common

import com.droidblossom.archive.domain.model.common.CapsuleDetail
import com.droidblossom.archive.domain.model.common.SocialCapsules

data class CapsuleDetailResponseDto(
    val address: String,
    val roadName: String,
    val capsuleSkinUrl: String,
    val content: String?,
    val createdDate: String,
    val profileUrl: String,
    val dueDate: String?,
    val isOpened: Boolean,
    val imageUrls: List<String>?,
    val videoUrls: List<String>?,
    val nickname: String,
    val title: String,
    val capsuleType: String,
){
    fun toModel() = CapsuleDetail(
        address = this.address,
        roadName = this.roadName,
        capsuleSkinUrl = this.capsuleSkinUrl,
        content = this.content ?: "",
        createdDate = this.createdDate,
        profileUrl = this.profileUrl,
        dueDate = this.dueDate,
        isOpened = this.isOpened,
        imageUrls = this.imageUrls,
        videoUrls = this.videoUrls,
        nickname = this.nickname,
        title =  this.title,
        capsuleType=this.capsuleType
    )

    fun toSocialCapsuleModel() = SocialCapsules(
        title = this.title,
        content = this.content ?: "",
        createdDate = this.createdDate,
        dueDate = this.dueDate,
        profileUrl = this.profileUrl,
        capsuleSkinUrl = this.capsuleSkinUrl,
        nickNameOrGroupName = this.nickname,
        roadName = this.roadName,
        address = this.address,
        hasThumbnail = !(imageUrls.isNullOrEmpty() && videoUrls.isNullOrEmpty()),
        thumbnailImage = imageUrls?.firstOrNull() ?: videoUrls?.firstOrNull(),
        isOpened = false
    )
}