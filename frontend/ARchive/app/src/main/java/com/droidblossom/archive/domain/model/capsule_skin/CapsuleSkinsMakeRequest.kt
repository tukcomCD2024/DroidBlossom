package com.droidblossom.archive.domain.model.capsule_skin

import com.droidblossom.archive.data.dto.capsule_skin.request.CapsuleSkinsMakeRequestDto
import java.io.File

data class CapsuleSkinsMakeRequest(
    val name : String,
    val skinImage : File,
    val motionName : String
){
    fun toDto()= CapsuleSkinsMakeRequestDto(
        name = this.name,
        skinImage = this.skinImage,
        motionName = this.motionName
    )
}
