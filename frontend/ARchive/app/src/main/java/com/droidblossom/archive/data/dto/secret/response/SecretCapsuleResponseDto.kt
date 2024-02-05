package com.droidblossom.archive.data.dto.secret.response

import com.droidblossom.archive.domain.model.common.MyCapsule

data class SecretCapsuleResponseDto(
    val address: String,
    val capsuleId: Long,
    val capsuleSkinUrl: String,
    val content: String,
    val createdAt: String,
    val dueDate: String?,
    val images: List<String>,
    val isOpened: Boolean,
    val nickname: String,
    val title: String,
    val videos: List<String>
){
    fun toModel()=MyCapsule(
        capsuleId = this.capsuleId,
        capsuleSkinUrl = this.capsuleSkinUrl,
        createdAt = this.createdAt,
        dueDate = this.dueDate,
        isOpened = this.isOpened,
        title =this.title
    )
}