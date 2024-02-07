package com.droidblossom.archive.domain.model.capsule

import com.droidblossom.archive.data.dto.common.CapsuleSummaryDto
import com.droidblossom.archive.domain.model.common.CapsuleMarker

data class NearbyCapsule(
    val capsules : List<CapsuleMarker>
)
