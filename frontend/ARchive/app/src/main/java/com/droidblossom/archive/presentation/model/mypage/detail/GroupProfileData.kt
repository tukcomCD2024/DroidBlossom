package com.droidblossom.archive.presentation.model.mypage.detail

data class GroupProfileData (
    val groupId:Long,
    val groupName : String,
    val groupDescription : String,
    val groupProfileUrl : String,
    val hasEditPermission : Boolean,
    val groupCapsuleNum: Int = 0,
    val groupMemberNum: String = "0"
)