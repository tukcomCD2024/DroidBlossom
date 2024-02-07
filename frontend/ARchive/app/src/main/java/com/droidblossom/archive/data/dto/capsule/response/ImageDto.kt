package com.droidblossom.archive.data.dto.capsule.response

import com.droidblossom.archive.domain.model.common.Image

data class ImageDto(
    val id: Int,
    val imageUrl: String
){
    fun toModel()=Image(
        id = this.id,
        imageUrl = this.imageUrl
    )
}