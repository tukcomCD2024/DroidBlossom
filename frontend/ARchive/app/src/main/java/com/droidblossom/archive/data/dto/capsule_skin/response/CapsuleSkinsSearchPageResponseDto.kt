package com.droidblossom.archive.data.dto.capsule_skin.response

import com.droidblossom.archive.data.dto.common.CapsuleSkinSummaryResponseDto
import com.droidblossom.archive.domain.model.capsule_skin.CapsuleSkinsSearchPageResponse

data class CapsuleSkinsSearchPageResponseDto(
    val skins : List<CapsuleSkinSummaryResponseDto>,
    val hasNext : Boolean,
    val hasPrevious : Boolean
){
    fun toModel() = CapsuleSkinsSearchPageResponse(
        skins = this.skins.map { it.toModel() },
        hasNext = this.hasNext,
        hasPrevious = this.hasPrevious,
    )
}
