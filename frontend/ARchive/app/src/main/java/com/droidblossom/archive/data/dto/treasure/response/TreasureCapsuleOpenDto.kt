package com.droidblossom.archive.data.dto.treasure.response

import com.droidblossom.archive.domain.model.treasure.response.TreasureCapsuleOpen
import com.droidblossom.archive.domain.model.treasure.response.TreasureOpenStatus

data class TreasureCapsuleOpenDto(
    val treasureOpenStatus: String,
    val treasureImageUrl: String
) {
    fun toModel() = TreasureCapsuleOpen(
        treasureOpenStatus = TreasureOpenStatus.fromDescription(this.treasureOpenStatus),
        treasureImageUrl = this.treasureImageUrl
    )
}