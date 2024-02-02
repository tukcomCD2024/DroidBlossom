package com.droidblossom.archive.domain.model.capsule

import com.droidblossom.archive.domain.model.common.Image

data class CapsuleImages(
    val hasNext: Boolean,
    val hasPrevious: Boolean,
    val images: List<Image>
)