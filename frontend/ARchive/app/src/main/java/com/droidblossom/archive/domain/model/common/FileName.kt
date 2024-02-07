package com.droidblossom.archive.domain.model.common

import com.droidblossom.archive.data.dto.common.FileNameDto

data class FileName(
    val extension: String,
    val fileName: String
) {
    fun toDto() = FileNameDto(
        extension = this.extension,
        fileName = this.fileName
    )
}