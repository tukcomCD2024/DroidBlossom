package com.droidblossom.archive.data.dto.s3.request

data class S3UrlRequestDto(
    val directory: String,
    val imageNames: List<String>?,
    val videoNames: List<String>?,
)