package com.droidblossom.archive.data.dto.treasure.response

import com.droidblossom.archive.domain.model.treasure.response.TreasureCapsuleOpen

data class TreasureCapsuleOpenDto (
    val treasureImageUrl: String
){
    fun toModel() = TreasureCapsuleOpen(
        treasureImageUrl = this.treasureImageUrl
    )
}