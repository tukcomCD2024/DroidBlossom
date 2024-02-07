package com.droidblossom.archive.data.dto.capsule_skin.response

import com.droidblossom.archive.data.dto.common.CapsuleSkinSummaryResponseDto
import com.droidblossom.archive.domain.model.capsule_skin.CapsuleSkinsPageResponse

data class CapsuleSkinsPageResponseDto (
    val skins : List<CapsuleSkinSummaryResponseDto>,
    val hasNext : Boolean
){
    fun toModel() = CapsuleSkinsPageResponse(
        skins = this.skins.map { it.toModel() },
        hasNext = this.hasNext,
    )
}