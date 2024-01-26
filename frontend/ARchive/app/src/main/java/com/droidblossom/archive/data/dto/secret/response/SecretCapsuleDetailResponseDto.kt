package com.droidblossom.archive.data.dto.secret.response

import com.droidblossom.archive.domain.model.secret.SecretCapsuleDetail

data class SecretCapsuleDetailResponseDto(
    val address: String,
    val capsuleSkinUrl: String,
    val content: String,
    val createdDate: String,
    val dueDate: String,
    val isOpened: Boolean,
    val mediaUrls: List<String>,
    val nickname: String,
    val title: String
){
    fun toModel() = SecretCapsuleDetail(
        address = this.address,
        capsuleSkinUrl = this.capsuleSkinUrl,
        content = this.content,
        createdDate = this.createdDate,
        dueDate = this.dueDate,
        isOpened = this.isOpened,
        mediaUrls = this.mediaUrls,
        nickname = this.nickname,
        title =  this.title
    )
}