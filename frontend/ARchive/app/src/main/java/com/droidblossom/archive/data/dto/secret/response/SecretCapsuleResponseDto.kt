package com.droidblossom.archive.data.dto.secret.response

import com.droidblossom.archive.domain.model.common.MyCapsule

data class SecretCapsuleResponseDto(
    val capsuleId: Long,
    val SkinUrl: String,
    val createdAt: String,
    val dueDate: String?,
    val isOpened: Boolean,
    val title: String,
    val type: String,
){
    fun toModel()=MyCapsule(
        capsuleId = this.capsuleId,
        capsuleSkinUrl = this.SkinUrl,
        createdDate = this.createdAt,
        dueDate = this.dueDate,
        isOpened = this.isOpened,
        title =this.title,
        capsuleType = this.type
    )
}