package com.droidblossom.archive.presentation.model.mypage.detail

data class GroupProfileData (
    val groupName : String,
    val groupDescription : String,
    val groupProfileUrl : String,
    val hasEditPermission : Boolean,
    val groupCreateTime : String,
    val groupCapsuleNum: String = "0",
    val groupMemberNum: String = "0"
)