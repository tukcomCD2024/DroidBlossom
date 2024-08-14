package com.droidblossom.archive.domain.model.group

data class GroupMember(
    val memberId: Long,
    val profileUrl: String,
    val nickname: String,
    val tag: String,
    val isOwner: Boolean,
    val isFriend: Boolean,
    val isFriendInviteToFriend: Boolean,
    val isFriendInviteToMe: Boolean
)
