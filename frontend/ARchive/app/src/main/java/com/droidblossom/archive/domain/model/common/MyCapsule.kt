package com.droidblossom.archive.domain.model.common

import com.droidblossom.archive.presentation.model.mypage.CapsuleData

data class MyCapsule (
    val capsuleId: Long,
    val capsuleSkinUrl: String,
    val createdDate: String,
    val dueDate: String?,
    var isOpened: Boolean,
    val title: String,
    val capsuleType: String
){
    fun toUIModel() = CapsuleData(
        capsuleId = this.capsuleId,
        capsuleSkinUrl = this.capsuleSkinUrl,
        createdDate = this.createdDate,
        dueDate = this.dueDate,
        isOpened = this.isOpened,
        title = this.title,
        capsuleType = this.capsuleType
    )
}