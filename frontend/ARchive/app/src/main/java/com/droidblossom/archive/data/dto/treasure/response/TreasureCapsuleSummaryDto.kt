package com.droidblossom.archive.data.dto.treasure.response

import com.droidblossom.archive.domain.model.treasure.response.TreasureCapsuleSummary

data class TreasureCapsuleSummaryDto(
    val skinUrl: String,
    val dueDate: String,
    val address: String,
    val roadName: String?,
) {
    fun toModel() = TreasureCapsuleSummary(
        skinUrl = this.skinUrl,
        dueDate = this.dueDate,
        address = this.address,
        roadName = if (this.roadName == "null" || this.roadName.isNullOrEmpty()) "" else this.roadName
    )
}