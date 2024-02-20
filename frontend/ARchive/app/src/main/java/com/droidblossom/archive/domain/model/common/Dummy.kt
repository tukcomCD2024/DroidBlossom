package com.droidblossom.archive.domain.model.common

import android.net.Uri

data class Dummy(
    val string: Uri?,
    val contentType:ContentType?,
    val last :Boolean
)

enum class ContentType{
    IMAGE, VIDEO
}