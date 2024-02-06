package com.droidblossom.archive.data.dto.common

import com.droidblossom.archive.domain.model.common.CapsuleSkinSummary

data class CapsuleSkinSummaryResponseDto (
    val id : Int,
    val skinUrl : String,
    val name : String,
    val createdAt : String
){
    fun toModel() = CapsuleSkinSummary(
        id = this.id,
        skinUrl = this.skinUrl,
        name = this.name,
        createdAt = this.createdAt,
        isClicked = false
    )
}
