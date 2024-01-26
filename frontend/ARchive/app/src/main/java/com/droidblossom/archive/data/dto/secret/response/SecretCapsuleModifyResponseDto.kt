package com.droidblossom.archive.data.dto.secret.response

import com.droidblossom.archive.domain.model.secret.SecretCapsuleModify

data class SecretCapsuleModifyResponseDto(
    val address: String,
    val dueDate: String,
    val id: Int,
    val isOpened: Boolean,
    val nickname: String,
    val skinUrl: String,
    val title: String
) {
    fun toModel() = SecretCapsuleModify(
        this.address, this.dueDate, this.id, this.isOpened, this.nickname, this.skinUrl, this.title
    )
}