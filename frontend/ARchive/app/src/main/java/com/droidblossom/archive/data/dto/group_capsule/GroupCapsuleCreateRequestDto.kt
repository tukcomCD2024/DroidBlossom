package com.droidblossom.archive.data.dto.group_capsule

import com.droidblossom.archive.data.dto.capsule.response.AddressDataDto

data class GroupCapsuleCreateRequestDto(
    val groupMemberIds : List<Long>,
    val capsuleSkinId: Long,
    val content: String,
    val directory: String,
    val dueDate: String?,
    val imageNames: List<String>?,
    val videoNames: List<String>?,
    val addressData: AddressDataDto,
    val latitude: Double,
    val longitude: Double,
    val title: String
)