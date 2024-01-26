package com.droidblossom.archive.domain.model.secret

import com.droidblossom.archive.data.dto.secret.request.SecretCapsuleCreateRequestDto
import com.droidblossom.archive.domain.model.common.FileName

data class SecretCapsuleCreateRequest(
    val capsuleSkinId: Int,
    val capsuleType: String,
    val content: String,
    val directory: String,
    val dueDate: String,
    val fileNames: List<FileName>,
    val latitude: Int,
    val longitude: Int,
    val title: String
) {
    fun toDto() = SecretCapsuleCreateRequestDto(
        this.capsuleSkinId,
        this.capsuleType,
        this.content,
        this.directory,
        this.dueDate,
        this.fileNames.map { it.toDto() },
        this.latitude,
        this.longitude,
        this.title
    )
}
