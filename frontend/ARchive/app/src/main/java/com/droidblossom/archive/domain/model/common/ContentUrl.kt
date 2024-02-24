package com.droidblossom.archive.domain.model.common

data class ContentUrl(
    val url: String,
    val contentType:ContentType,
){
    constructor() : this("",ContentType.IMAGE)
}