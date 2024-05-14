package com.droidblossom.archive.domain.model.setting

data class Agree(
    val title : String,
    val content : String,
    var isOpen : Boolean = false,
)
