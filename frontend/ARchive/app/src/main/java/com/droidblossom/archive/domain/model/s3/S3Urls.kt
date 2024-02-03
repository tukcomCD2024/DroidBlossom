package com.droidblossom.archive.domain.model.s3

data class S3Urls(
    val preSignedImageUrls: List<String>,
    val preSignedVideoUrls: List<String>
)
