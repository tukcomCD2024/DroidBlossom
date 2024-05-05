package com.droidblossom.archive.domain.model.friend

data class FriendsSearchResponse (
    val id : Long,
    val profileUrl : String,
    val nickname : String,
    val isFriend : Boolean,
    var isFriendInviteToFriend : Boolean,
    var isFriendInviteToMe : Boolean,
    var isChecked : Boolean,
    val name : String
)