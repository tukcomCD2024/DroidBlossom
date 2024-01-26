package com.droidblossom.archive.data.dto.secret.response

import com.droidblossom.archive.domain.model.secret.SecretCapsuleSummery

data class SecretCapsuleSummeryDto(
    val address: String,
    val dueDate: String,
    val id: Int,
    val isOpened: Boolean,
    val nickname: String,
    val skinUrl: String,
    val title: String
){
    fun toModel() = SecretCapsuleSummery(
        address = this.address,
        dueDate = this.dueDate,
        id = this.id,
        isOpened =this.isOpened,
        nickname = this.nickname,
        skinUrl = this.skinUrl,
        title = this.title
    )
}