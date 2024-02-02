package com.droidblossom.archive.data.dto.secret.request

import com.droidblossom.archive.data.dto.capsule.response.AddressDataDto
import com.droidblossom.archive.data.dto.common.FileNameDto

data class SecretCapsuleCreateRequestDto(
    val capsuleSkinId: Long,
    val content: String,
    val directory: String,
    val dueDate: String,
    val fileNames: List<FileNameDto>,
    val addressData: AddressDataDto,
    val latitude: Double,
    val longitude: Double,
    val title: String
)