package com.droidblossom.archive.data.dto.open.response

import com.droidblossom.archive.data.dto.common.CapsuleDetailResponseDto
import com.droidblossom.archive.domain.model.open.PublicCapsuleSliceResponse
import com.droidblossom.archive.domain.model.s3.S3Urls

data class PublicCapsuleSliceResponseDto(
    val publicCapsules: List<PublicCapsuleResponseDto>,
    val hasNext: Boolean
){
    fun toModel()= PublicCapsuleSliceResponse(
        publicCapsules = this.publicCapsules.map { it.toModel() },
        hasNext = this.hasNext
    )
}