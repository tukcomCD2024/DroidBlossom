package com.droidblossom.archive.domain.model.setting

data class Notice(
    val title: String,
    val date : String,
    val contents : List<NoticeContent>,
    var isOpen : Boolean = false
)
