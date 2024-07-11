package com.droidblossom.archive.data.dto.group_capsule

import com.droidblossom.archive.domain.model.common.CapsuleDetail
import com.droidblossom.archive.domain.model.group_capsule.GroupCapsuleMember

data class GroupCapsuleDetailResponseDto(
    val address: String,
    val roadName: String?,
    val capsuleSkinUrl: String,
    val members : List<DummyMember>,
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
    val hasEditPermission: Boolean,
    val hasDeletePermission: Boolean
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
        isOwner = this.hasDeletePermission
    )
}

data class DummyMember(
    val memberId: Long,
    val nickname : String,
    val profileUrl : String,
    val isOpened : Boolean,
    val isGroupOwner : Boolean
){
    fun toModel() = GroupCapsuleMember(
        memberId = this.memberId,
        nickname = this.nickname,
        profileUrl = this.profileUrl,
        isOpened = this.isOpened,
        isGroupOwner = this.isGroupOwner
    )
}
