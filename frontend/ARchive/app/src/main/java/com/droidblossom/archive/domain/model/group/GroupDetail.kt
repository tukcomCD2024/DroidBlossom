package com.droidblossom.archive.domain.model.group

import com.droidblossom.archive.presentation.model.mypage.detail.GroupProfileData

data class GroupDetail (
    val groupName: String,
    val groupProfileUrl: String,
    val groupDescription: String,
    val createdAt: String,
    val members: List<GroupMember>,
)

fun GroupDetail.toGroupProfileData() = GroupProfileData(
    groupId = -1,
    groupName = groupName,
    groupDescription = groupDescription,
    groupProfileUrl = groupProfileUrl,
    hasEditPermission = false,
    groupCapsuleNum = -1,
    groupMemberNum = members.size.toString(),
    groupCreateTime = createdAt
)