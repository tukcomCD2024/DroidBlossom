package com.droidblossom.archive.domain.model.common

data class CapsuleSkinSummary (
    val id : Long,
    val skinUrl : String,
    val name : String,
    val createdAt : String,
    var isClicked :Boolean
)