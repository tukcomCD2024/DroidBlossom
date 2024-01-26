package com.droidblossom.archive.data.dto.s3.request

import com.droidblossom.archive.data.dto.common.FileNameDto

data class S3UrlRequestDto(
    val directory: String,
    val fileMetaDataList: List<FileNameDto>
)