package com.droidblossom.archive.data.dto.group_capsule

import com.droidblossom.archive.domain.model.common.CapsuleDetail

data class GroupCapsuleDetailResponseDto(
    val address: String,
    val roadName: String?,
    val capsuleSkinUrl: String,
    val members : List<GroupCapsuleMemberDto>,
    val content: String?,
    val createdDate: String,
    val latitude: Double,
    val longitude: Double,
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
        roadName = this.roadName ?: "",
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
        capsuleType=this.capsuleType,
        members = this.members.map { it.toModel() },
    )
}