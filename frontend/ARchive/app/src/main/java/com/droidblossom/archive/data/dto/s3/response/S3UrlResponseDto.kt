package com.droidblossom.archive.data.dto.s3.response

import com.droidblossom.archive.domain.model.s3.S3Urls

data class S3UrlResponseDto(
    val preSignedUrls: List<String>
){
    fun toModel()=S3Urls(
        preSignedUrls = this.preSignedUrls
    )
}