package com.droidblossom.archive.domain.model.friend

import com.droidblossom.archive.presentation.model.mypage.friend.AddTagSearchFriendUIModel

data class FriendsSearchResponse (
    val createdAt : String,
    val id : Long,
    val profileUrl : String,
    val nickname : String,
    val isFriend : Boolean,
    var isFriendInviteToFriend : Boolean,
    var isFriendInviteToMe : Boolean,
    var isChecked : Boolean,
    val name : String,
    val tag: String,
){
    fun toAddTagSearchFriendUIModel() = AddTagSearchFriendUIModel(
        id = this.id,
        profileUrl = this.profileUrl,
        nickname = this.nickname,
        isFriend = this.isFriend,
        isFriendInviteToFriend = this.isFriendInviteToFriend,
        isFriendInviteToMe = this.isFriendInviteToMe,
        name = this.name,
        tag = this.tag
    )
}