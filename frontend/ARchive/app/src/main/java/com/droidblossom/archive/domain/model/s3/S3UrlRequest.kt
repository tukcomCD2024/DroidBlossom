package com.droidblossom.archive.domain.model.s3

import com.droidblossom.archive.data.dto.s3.request.S3UrlRequestDto
import com.droidblossom.archive.domain.model.common.FileName

data class S3UrlRequest(
    val directory: String,
    val fileMetaDataList: List<FileName>
){
    fun toDto() = S3UrlRequestDto(
        directory = this.directory,
        fileMetaDataList = this.fileMetaDataList.map { it.toDto() }
    )
}
