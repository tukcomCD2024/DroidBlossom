package com.droidblossom.archive.data.dto.common

data class CapsuleSummaryDto(
    val id: Int,
    val longitude: Double,
    val latitude: Double,
    val nickname: String,
    val capsuleSkinUrl: String,
    val title: String,
    val dueDate: String,
    val capsuleType: String,
)
