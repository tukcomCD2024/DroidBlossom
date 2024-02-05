package com.droidblossom.archive.data.dto.secret.response

import com.droidblossom.archive.domain.model.secret.SecretCapsuleSummary

data class SecretCapsuleSummaryResponseDto(
    val nickname: String,
    val profileUrl: String,
    val skinUrl: String,
    val title: String,
    val dueDate: String,
    val address: String,
    val roadName: String,
    val isOpened: Boolean,
    val createdAt: String
){
    fun toModel() = SecretCapsuleSummary(
        nickname = this.nickname,
        profileUrl = this.profileUrl,
        skinUrl = this.skinUrl,
        title = this.title,
        dueDate =this.dueDate,
        address = this.address,
        roadName = this.roadName,
        isOpened = this.isOpened,
        createdAt = this.createdAt,
    )
}