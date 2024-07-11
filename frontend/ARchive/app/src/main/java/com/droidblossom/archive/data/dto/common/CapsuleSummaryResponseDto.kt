package com.droidblossom.archive.data.dto.common

import com.droidblossom.archive.domain.model.common.CapsuleSummaryResponse

data class CapsuleSummaryResponseDto(
    val nickname: String,
    val profileUrl: String,
    val skinUrl: String,
    val title: String,
    val dueDate: String?,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val roadName: String?,
    val isOpened: Boolean,
    val createdAt: String,
    val isOwner: Boolean?
){
    fun toModel() = CapsuleSummaryResponse(
        nicknameOrGroupName = this.nickname,
        profileOrGroupProfileUrl = this.profileUrl,
        skinUrl = this.skinUrl,
        title = this.title,
        dueDate = this.dueDate ?: "",
        address = this.address,
        roadName = this.roadName ?: "",
        isOpened = this.isOpened,
        createdAt = this.createdAt,
        isOwner = this.isOwner ?: true
    )
}