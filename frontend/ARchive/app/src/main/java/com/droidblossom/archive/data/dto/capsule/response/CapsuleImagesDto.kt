package com.droidblossom.archive.data.dto.capsule.response

import com.droidblossom.archive.domain.model.capsule.CapsuleImages

data class CapsuleImagesDto(
    val hasNext: Boolean,
    val hasPrevious: Boolean,
    val images: List<ImageDto>
){
    fun toModel()=CapsuleImages(
        hasNext = this.hasNext,
        hasPrevious = this.hasPrevious,
        images = this.images.map { it.toModel() }
    )
}