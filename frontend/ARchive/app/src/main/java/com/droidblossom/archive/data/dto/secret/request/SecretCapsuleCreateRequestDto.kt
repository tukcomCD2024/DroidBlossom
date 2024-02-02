package com.droidblossom.archive.data.dto.secret.request

import com.droidblossom.archive.data.dto.capsule.response.AddressDataDto
import com.droidblossom.archive.data.dto.common.FileNameDto

data class SecretCapsuleCreateRequestDto(
    val capsuleSkinId: Int,
    val capsuleType: String,
    val content: String,
    val directory: String,
    val dueDate: String,
    val fileNames: List<FileNameDto>,
    val addressDataDto: List<AddressDataDto>,
    val latitude: Int,
    val longitude: Int,
    val title: String
)