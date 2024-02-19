package com.droidblossom.archive.data.dto.capsule_skin.request

data class CapsuleSkinsMakeRequestDto (
    val skinName : String,
    val imageUrl : String,
    val directory : String,
    val motionName : String?,
    val retarget : String?
)