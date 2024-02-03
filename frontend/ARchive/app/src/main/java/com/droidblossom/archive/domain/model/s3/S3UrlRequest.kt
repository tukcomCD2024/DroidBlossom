package com.droidblossom.archive.domain.model.s3

import com.droidblossom.archive.data.dto.s3.request.S3UrlRequestDto

data class S3UrlRequest(
    val directory: String,
    val imageNames: List<String>,
    val videoNames: List<String>
){
    fun toDto() = S3UrlRequestDto(
        directory = this.directory,
        imageNames = this.imageNames.ifEmpty { null },
        videoNames = this.videoNames.ifEmpty { null }
    )
}
