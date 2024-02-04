package com.droidblossom.archive.data.dto.secret.response

import com.droidblossom.archive.domain.model.secret.SecretCapsuleSummary

data class SecretCapsuleSummaryResponseDto(
    val nickname: String,
    val skinUrl: String,
    val title: String,
    val dueDate: String,
    val address: String,
    val isOpened: Boolean,
    val createdAt: String
){
    fun toModel() = SecretCapsuleSummary(
        nickname = this.nickname,
        skinUrl = this.skinUrl,
        title = this.title,
        dueDate =this.dueDate,
        address = this.address,
        isOpened = this.isOpened,
        createdAt = this.createdAt,
    )
}