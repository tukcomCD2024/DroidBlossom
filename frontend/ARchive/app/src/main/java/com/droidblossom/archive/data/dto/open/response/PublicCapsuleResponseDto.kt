package com.droidblossom.archive.data.dto.open.response

import com.droidblossom.archive.data.dto.common.CapsuleDetailResponseDto
import com.droidblossom.archive.domain.model.common.SocialCapsules
import com.droidblossom.archive.domain.model.open.PublicCapsuleSliceResponse

data class PublicCapsuleResponseDto (
    val capsuleId: Long,
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
    fun toModel()= SocialCapsules(
        capsuleId = this.capsuleId,
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
        thumbnailImage = imageUrls?.firstOrNull() ?: videoUrls?.firstOrNull() ?: "",
        isOpened = this.isOpened
    )
}