package com.droidblossom.archive.domain.model.treasure.response

import com.droidblossom.archive.domain.model.common.CapsuleSummaryResponse

data class TreasureCapsuleSummary(
    val skinUrl: String,
    val dueDate: String,
    val address: String,
    val roadName: String,
){
    fun toCapsuleSummaryResponseModel() = CapsuleSummaryResponse(
        nicknameOrGroupName = "ARchive팀",
        profileOrGroupProfileUrl = "",
        skinUrl = this.skinUrl,
        title = "보물캡슐을 발견했어요!!",
        dueDate = "",
        address = this.address,
        roadName = this.roadName,
        isOpened = false,
        createdAt = ""
    )
}