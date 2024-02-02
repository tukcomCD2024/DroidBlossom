package com.droidblossom.archive.domain.model.common

import com.droidblossom.archive.data.dto.common.CapsuleSummaryDto
import com.droidblossom.archive.presentation.ui.home.HomeFragment
import com.droidblossom.archive.util.CapsuleTypeUtils

data class CapsuleSummary(
    val id: Int,
    val longitude: Double,
    val latitude: Double,
    val nickname: String,
    val capsuleSkinUrl: String,
    val title: String,
    val dueDate: String,
    val capsuleType: HomeFragment.CapsuleType,
){
    fun toDto() = CapsuleSummaryDto(
        id = this.id,
        longitude = this.longitude,
        latitude = this.latitude,
        nickname = this.nickname,
        capsuleSkinUrl = this.capsuleSkinUrl,
        title = this.title,
        dueDate = this.dueDate,
        capsuleType = CapsuleTypeUtils.enumToString(capsuleType),
    )
}
