package com.droidblossom.archive.data.dto.capsule.response

import com.droidblossom.archive.data.dto.common.CapsuleSummaryDto
import com.droidblossom.archive.domain.model.capsule.CapsuleAnchors
import com.droidblossom.archive.domain.model.capsule.CapsuleMarkers

data class NearbyCapsuleResponseDto (
    val capsules : List<CapsuleSummaryDto>
){
    fun toMarkerModel() = CapsuleMarkers(
        capsuleMarkers = this.capsules.map { it.toMarkerModel() }
    )
    fun toAnchorModel() = CapsuleAnchors(
        capsuleAnchors = this.capsules.map { it.toAnchorModel() }
    )
}