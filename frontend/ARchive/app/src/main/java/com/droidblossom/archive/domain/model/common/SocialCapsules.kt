package com.droidblossom.archive.domain.model.common

data class SocialCapsules (
    val capsuleId:Long,
    val title:String,
    val content:String,
    val createdDate:String,
    val dueDate:String?,
    val profileUrl:String,
    val capsuleSkinUrl: String,
    val nickNameOrGroupName:String,
    val thumbnailImage:String?,
    val roadName: String,
    val address: String,
    val hasThumbnail: Boolean,
    val isOpened:Boolean,
)

//val capsuleType: String,