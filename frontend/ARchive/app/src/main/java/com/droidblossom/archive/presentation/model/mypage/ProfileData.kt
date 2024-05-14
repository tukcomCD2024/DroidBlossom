package com.droidblossom.archive.presentation.model.mypage

data class ProfileData (
    val profileId:Long,
    val nickname : String,
    val profileUrl : String,
    val tag : String,
    val friendCount: Int,
    val groupCount: Int
)