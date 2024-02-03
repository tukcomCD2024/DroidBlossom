package com.droidblossom.archive.domain.model.s3

import com.droidblossom.archive.data.dto.s3.request.S3UrlRequestDto

data class S3UrlRequest(
    val directory: String,
    val imageUrls: List<String>,
    val videoUrls: List<String>
){
    fun toDto() = S3UrlRequestDto(
        directory = this.directory,
        imageUrls = this.imageUrls.ifEmpty { null },
        videoUrls = this.videoUrls.ifEmpty { null }
    )
}
