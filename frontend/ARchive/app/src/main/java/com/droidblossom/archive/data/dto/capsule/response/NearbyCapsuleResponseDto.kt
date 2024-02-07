package com.droidblossom.archive.data.dto.capsule.response

import com.droidblossom.archive.data.dto.common.CapsuleSummaryDto
import com.droidblossom.archive.domain.model.capsule.NearbyCapsule

data class NearbyCapsuleResponseDto (
    val capsules : List<CapsuleSummaryDto>
){
    fun toModel() = NearbyCapsule(
        capsules = this.capsules.map { it.toModel() }
    )
}