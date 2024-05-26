package com.droidblossom.archive.domain.model.group_capsule

import com.droidblossom.archive.data.dto.group_capsule.GroupCapsuleCreateRequestDto
import com.droidblossom.archive.domain.model.common.AddressData

data class GroupCapsuleCreateRequest(
    val groupMemberIds: List<Long>,
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
    fun toDto() = GroupCapsuleCreateRequestDto(
        groupMemberIds = this.groupMemberIds,
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
