package com.droidblossom.archive.data.dto.capsule_skin.request

import java.io.File

data class CapsuleSkinsMakeRequestDto (
    val name : String,
    val skinImage : File,
    val motionName : String
)