package com.droidblossom.archive.presentation.model.mypage.friend

data class AddTagSearchFriendUIModel(
    val id : Long,
    val profileUrl : String,
    val nickname : String,
    val isFriend : Boolean,
    var isFriendInviteToFriend : Boolean,
    var isFriendInviteToMe : Boolean,
    val name : String,
    val tag: String
)
