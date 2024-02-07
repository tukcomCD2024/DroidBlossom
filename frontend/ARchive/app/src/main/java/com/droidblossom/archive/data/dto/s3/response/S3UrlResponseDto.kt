package com.droidblossom.archive.data.dto.s3.response

import com.droidblossom.archive.domain.model.s3.S3Urls

data class S3UrlResponseDto(
    val preSignedImageUrls: List<String>,
    val preSignedVideoUrls: List<String>
){
    fun toModel()=S3Urls(
        preSignedImageUrls = this.preSignedImageUrls,
        preSignedVideoUrls = this.preSignedVideoUrls
    )
}