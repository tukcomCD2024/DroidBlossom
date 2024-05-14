package com.droidblossom.archive.domain.model.common

import com.droidblossom.archive.data.dto.common.CapsuleCreateRequestDto

data class CapsuleCreateRequest(
    val capsuleSkinId: Long,
    val content: String,
    val directory: String,
    val dueDate: String,
    val imageNames: List<String>,
    val videoNames: List<String>,
    val addressData: AddressData,
    val latitude: Double,
    val longitude: Double,
    val title: String
) {
    fun toDto() = CapsuleCreateRequestDto(
        capsuleSkinId = this.capsuleSkinId,
        content = this.content,
        directory = this.directory,
        dueDate = this.dueDate.ifEmpty { null },
        imageNames = this.imageNames.ifEmpty { null },
        videoNames = this.videoNames.ifEmpty { null },
        addressData = this.addressData.toDto(),
        latitude = this.latitude,
        longitude = this.longitude,
        title = this.title
    )
}
