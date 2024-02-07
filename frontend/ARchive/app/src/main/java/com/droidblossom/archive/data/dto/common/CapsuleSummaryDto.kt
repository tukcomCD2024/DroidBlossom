package com.droidblossom.archive.data.dto.common

import com.droidblossom.archive.domain.model.common.CapsuleMarker
import com.droidblossom.archive.presentation.ui.home.HomeFragment

data class CapsuleSummaryDto(
    val id: Long,
    val longitude: Double,
    val latitude: Double,
    val nickname: String,
    val capsuleSkinUrl: String,
    val title: String,
    val dueDate: String,
    val capsuleType: String,
){
    fun toModel() = CapsuleMarker(
        id = this.id,
        longitude = this.longitude,
        latitude = this.latitude,
        capsuleType = HomeFragment.CapsuleType.valueOf(capsuleType),
        skinUrl = this.capsuleSkinUrl
    )
}
