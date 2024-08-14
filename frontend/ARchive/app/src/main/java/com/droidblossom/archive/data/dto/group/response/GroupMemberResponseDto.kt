package com.droidblossom.archive.data.dto.group.response

import com.droidblossom.archive.domain.model.group.GroupMember

data class GroupMemberResponseDto(
    val memberId: Long,
    val profileUrl: String,
    val nickname: String,
    val tag: String,
    val isOwner: Boolean,
    val isFriend: Boolean,
    val isFriendInviteToFriend: Boolean,
    val isFriendInviteToMe: Boolean
){
    fun toGroupDetailModel() = GroupMember(
        memberId = this.memberId,
        profileUrl = this.profileUrl,
        nickname = this.nickname,
        tag = this.tag,
        isOwner = this.isOwner,
        isFriend = this.isFriend,
        isFriendInviteToFriend = this.isFriendInviteToFriend,
        isFriendInviteToMe = this.isFriendInviteToMe
    )

    fun toGroupMemberListModel() = GroupMember(
        memberId = this.memberId,
        profileUrl = this.profileUrl,
        nickname = this.nickname,
        tag = this.tag,
        isOwner = this.isOwner,
        isFriend = this.isFriend,
        isFriendInviteToFriend = this.isFriendInviteToFriend,
        isFriendInviteToMe = this.isFriendInviteToMe
    )
}
